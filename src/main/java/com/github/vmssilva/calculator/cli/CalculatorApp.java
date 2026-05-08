package com.github.vmssilva.calculator.cli;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.vmssilva.calculator.cli.repl.History;
import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.Renderer;
import com.github.vmssilva.calculator.cli.repl.command.Command;
import com.github.vmssilva.calculator.cli.repl.command.CommandContext;
import com.github.vmssilva.calculator.cli.repl.command.ThemeCommand;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.style.Color;
import com.github.vmssilva.calculator.cli.repl.terminal.Terminal;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalInfo;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalSession;
import com.github.vmssilva.calculator.cli.repl.terminal.UnixTerminal;
import com.github.vmssilva.calculator.engine.context.ApplicationContext;
import com.github.vmssilva.calculator.engine.runtime.CalculatorRuntime;
import com.github.vmssilva.calculator.engine.std.value.ListValue;
import com.github.vmssilva.calculator.engine.std.value.StringValue;
import com.github.vmssilva.calculator.engine.std.value.Value;

public class CalculatorApp {

  private static ApplicationContext context = new ApplicationContext();
  private static final CalculatorRuntime runtime = new CalculatorRuntime();

  private static final Terminal terminal = new UnixTerminal();
  private static final TerminalSession session = new TerminalSession(terminal);
  private static final State state = new State();
  private static final Renderer renderer = new Renderer(state, session.out());

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

        if (handleCommand(input)) {
          continue;
        }

        try {
          String result = "";
          Value<?> value = evaluate(input);
          state.isError = false;

          result = value.toString();

          if (value instanceof ListValue l) {
            result = l.unwrap().stream().map(m -> m.unwrap().toString()).collect(Collectors.joining("\r\n"));
          }

          session.out().setColor(state.theme.successColor());
          session.out().write("\n");
          session.out().clearLine();
          session.out().write(result + "\n");
          session.out().setColor(Color.RESET);

        } catch (Exception e) {
          state.isError = true;
          session.out().setColor(state.theme.errorColor());
          session.out().write("\n");
          session.out().clearLine();
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
      case "reset" -> resetApplication();
      default -> handleExtendedCommand(input.substring(1));
    }

    return true;
  }

  private static void resetApplication() {
    state.resetInput();
    state.history = new History();
    context = new ApplicationContext();
  }

  private static void handleExtendedCommand(String cmd) {

    if (cmd.startsWith("theme ")) {
      String arg = cmd.substring("theme ".length()).trim();
      new ThemeCommand(arg).execute(new CommandContext(state, session.out()));
      return;
    }

    session.out()
        .write("\r\n" + "Unknown command\n")
        .flush();
  }

  private static Value<?> evaluate(String input) {
    try {

      Value<?> result = runtime.evaluate(input, context);
      state.history.add(input);

      return result;

    } catch (Exception e) {
      throw e;
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

        session.out().clearLine().write(renderer.getPrompt() + state.buffer.toString());

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

  private static Value<?> evaluateSafe(String expression) {

    ApplicationContext previewContext = new ApplicationContext();
    previewContext.currentScope().setParent(context.currentScope());
    previewContext.pushScope();

    try {
      return runtime.evaluate(expression, previewContext);
    } catch (Exception e) {
      return new StringValue("");
    } finally {
      previewContext.popScope();
    }
  }

}
