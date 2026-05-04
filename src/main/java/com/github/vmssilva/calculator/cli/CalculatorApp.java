package com.github.vmssilva.calculator.cli;

import java.io.IOException;
import java.util.List;

import com.github.vmssilva.calculator.cli.helper.Ansi;
import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.Renderer;
import com.github.vmssilva.calculator.cli.repl.command.Command;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalControl;
import com.github.vmssilva.calculator.cli.repl.terminal.UnixTerminal;
import com.github.vmssilva.calculator.engine.context.ApplicationContext;
import com.github.vmssilva.calculator.engine.runtime.CalculatorRuntime;

public class CalculatorApp {

  private static final ApplicationContext context = new ApplicationContext();
  private static final CalculatorRuntime runtime = new CalculatorRuntime();

  private static final State state = new State();
  private static final TerminalControl terminal = new TerminalControl(new UnixTerminal());
  private static final Renderer renderer = new Renderer(state, "calc> ");

  public static void main(String[] args) {

    if (args.length > 0) {
      runCli(args[0]);
      return;
    }

    repl();
  }

  private static void runCli(String expr) {
    try {
      System.out.println(runtime.evaluate(expr, context));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }

  private static void repl() {

    state.mapper.register(List.of("CTRL+C", "CTRL+D"), (s) -> exit(1, ""));

    try {
      terminal.init();
      state.effects.hideCursor();

      while (true) {

        renderFrame();

        String input = getLine();
        state.effects.write(Ansi.clearLine() + renderer.getPrompt() + input + "\n");

        if (handleCommand(input)) {
          continue;
        }

        evaluate(input);

      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      shutdown();
    }
  }

  private static void renderFrame() {
    state.effects.write(renderer.getPrompt());
  }

  private static boolean handleCommand(String input) {
    if (!input.startsWith("\\"))
      return false;

    switch (input.substring(1)) {
      case "q", "quit", "exit" -> exit(0, "Bye!");
      case "clear" -> state.effects.clearScreen();
      default -> state.effects.write("Unknown command\n");
    }

    return true;
  }

  private static void evaluate(String input) {
    try {
      var result = runtime.evaluate(input, context);
      state.history.add(input);
      state.effects.write("\r" + Ansi.clearLine() + result + "\n");
    } catch (Exception e) {
      state.effects.write("\r" + e.getMessage() + "\n");
    }
  }

  private static void shutdown() {
    state.effects.showCursor();
    try {
      terminal.restore();
    } catch (Exception e) {
      state.effects.write("Error restoring terminal\n");
    }
  }

  private static String getLine() throws IOException {

    while (true) {

      renderer.render();

      Key key = terminal.read();

      // 1. primeiro verifica ENTER (ANTES de qualquer mutação)
      if (key.type() == KeyType.ENTER) {
        String result = state.buffer.toString().trim();

        if (result.isEmpty())
          continue;

        state.resetInput();

        return result;
      }

      // 2. depois processa comandos normais
      Command command = state.mapper.map(key);
      if (command == null)
        continue;

      command.execute(state);

      // 3. preview só depois da mutação
      preview(state.buffer.toString());
    }
  }

  private static void exit(int code, String msg) {
    shutdown();
    if (!msg.isEmpty())
      System.out.println("\r" + msg);
    System.exit(code);
  }

  private static String evaluateSafe(String expression) {
    try {
      return runtime.evaluate(expression, context).toString();
    } catch (Exception e) {
      return "";
    }
  }

  private static void preview(String pre) {

    try {

      if (pre.isEmpty()) {
        state.effects.write(
            Ansi.saveCursor()
                + Ansi.cursorDown(1)
                + Ansi.clearLine()
                + Ansi.restoreCursor());
        return;
      }

      state.effects.write(
          Ansi.saveCursor()
              + Ansi.cursorDown(1)
              + "\r"
              + Ansi.clearLine()
              + Ansi.Color.GRAY
              + "→ "
              + evaluateSafe(pre)
              + Ansi.RESET
              + Ansi.restoreCursor());

    } catch (Exception e) {

      state.effects.write(
          Ansi.saveCursor()
              + "\n"
              + Ansi.clearLine()
              + Ansi.restoreCursor());
    }
  }

}
