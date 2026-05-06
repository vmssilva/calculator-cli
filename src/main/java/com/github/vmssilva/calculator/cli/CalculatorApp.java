package com.github.vmssilva.calculator.cli;

import java.io.IOException;
import java.util.List;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.Renderer;
import com.github.vmssilva.calculator.cli.repl.command.Command;
import com.github.vmssilva.calculator.cli.repl.command.CommandContext;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.style.Color;
import com.github.vmssilva.calculator.cli.repl.style.DefaultTheme;
import com.github.vmssilva.calculator.cli.repl.style.Theme;
import com.github.vmssilva.calculator.cli.repl.terminal.Terminal;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalInfo;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalSession;
import com.github.vmssilva.calculator.cli.repl.terminal.UnixTerminal;
import com.github.vmssilva.calculator.engine.context.ApplicationContext;
import com.github.vmssilva.calculator.engine.runtime.CalculatorRuntime;

public class CalculatorApp {

  private static final ApplicationContext context = new ApplicationContext();
  private static final CalculatorRuntime runtime = new CalculatorRuntime();

  private static final Terminal terminal = new UnixTerminal();
  private static final TerminalSession session = new TerminalSession(terminal);
  private static final State state = new State();
  private static final Theme theme = new DefaultTheme();
  private static final Renderer renderer = new Renderer(state, session.out(), theme);

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

      session.terminal().setup();
      session.out().hideCursor();

      while (true) {

        renderFrame();

        String input = getLine();

        session.out().clearLine();
        renderFrame();
        session.out().write(input + "\n");

        if (handleCommand(input)) {
          continue;
        }

        try {
          var result = evaluate(input);
          session.out().clearLine();

          session.out().setColor(theme.successColor());
          session.out().write(theme.successSymbol() + " ");

          session.out().write(result + "\n");
          session.out().setColor(Color.RESET);

        } catch (Exception e) {
          session.out().clearLine();
          session.out().setColor(theme.errorColor());
          session.out().write(theme.errorSymbol() + " ");
          session.out().write(e.getMessage() + "\n");
          session.out().setColor(Color.RESET);
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      shutdown();
    }
  }

  private static void renderFrame() {
    session.out().write(renderer.getPrompt());
  }

  private static boolean handleCommand(String input) {
    if (!input.startsWith("\\"))
      return false;

    switch (input.substring(1)) {
      case "q", "quit", "exit" -> exit(0, "Bye!");
      case "clear" -> session.out().clearScreen();
      default -> session.out().write("Unknown command\n").flush();
    }

    return true;
  }

  private static String evaluate(String input) {
    try {
      var result = runtime.evaluate(input, context);
      state.history.add(input);

      return result.toString();

      // session.out().clearLine().write(result + "\n");
    } catch (Exception e) {
      throw e;
      // session.out().clearLine().write(e.getMessage() + "\n");
    }
  }

  private static void shutdown() {
    session.out().showCursor();
    try {
      session.terminal().restore();
    } catch (Exception e) {
      session.out().write("Error restoring terminal\n");
    }
  }

  private static String getLine() throws IOException {

    while (true) {

      renderer.render();

      Key key = session.in().read();

      if (key.type() == KeyType.ENTER) {
        String result = state.buffer.toString().trim();

        if (result.isEmpty())
          continue;

        state.resetInput();

        return result;
      }

      Command command = state.mapper.map(key);
      if (command == null)
        continue;

      command.execute(new CommandContext(state, session.out()));

      int[] pos = TerminalInfo.cursorPosition(session.terminal().in());

      renderer.renderPreview(state.buffer.toString(), (value) -> evaluateSafe(value), pos[0] + 1, 0);

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

}
