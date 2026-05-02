package com.github.vmssilva.calculator.cli;

import java.io.IOException;
import java.util.List;

import com.github.vmssilva.calculator.cli.helper.Ansi;
import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.Renderer;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalControl;
import com.github.vmssilva.calculator.cli.repl.terminal.UnixTerminal;
import com.github.vmssilva.calculator.engine.context.ApplicationContext;
import com.github.vmssilva.calculator.engine.exception.ValueErrorException;
import com.github.vmssilva.calculator.engine.runtime.CalculatorRuntime;
import com.github.vmssilva.calculator.engine.value.Value;

public class CalculatorApp {

  private static final ApplicationContext context = new ApplicationContext();
  private static final CalculatorRuntime runtime = new CalculatorRuntime();
  private static final State state = new State();
  private static final TerminalControl terminal = new TerminalControl(new UnixTerminal());
  private static final Renderer renderer = new Renderer(state, "calc> ");

  public static void main(String[] args) {

    if (args.length > 0) {
      try {
        var res = evaluate(args[0], context);
        System.out.println(res);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
    } else {
      repl();
    }
  }

  private static void handleCommand(String cmd) {
    switch (cmd) {
      case "q", "quit", "exit" -> exit(0, "Bye!");
      default -> throw new UnsupportedOperationException("Invalid option");
    }
  }

  private static Value evaluate(String expression, ApplicationContext context) {
    try {
      return runtime.evaluate(expression, context);
    } catch (Exception e) {
      throw new ValueErrorException(e.getMessage());
    }
  }

  private static void repl() {

    state.mapper.register(List.of(Key.of("CTRL+C"), Key.of("CTRL+D")), (s) -> {
      exit(1, "");
    });

    try {
      terminal.init();
      state.effects.hideCursor();
      String input = "";

      while (true) {

        input = readLine();

        if (input.startsWith("\\")) {
          try {
            handleCommand(input.substring(1));
          } catch (Exception e) {
            System.out.println(Ansi.clearLine() + e.getMessage());
            continue;
          }
        }

        try {
          var result = evaluate(input, context);
          state.history.add(input);
          System.out.printf("\r%s\n", result);
        } catch (ValueErrorException e) {
          System.out.printf("\r%s\n", "Invalid expression");
          continue;
        }
      }
    } catch (Exception e) {
    } finally {
      state.effects.showCursor();
      try {
        terminal.restore();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static void preview(String pre) {

    if (pre.isEmpty()) {
      System.out.printf("\033[1B\r\033[2K\033[1A");
      return;
    }

    try {
      System.out.print("\033[1B\r\033[2K\033[90m" + evaluate(pre, context).toString() + "\033[0m" + "\033[1A");
    } catch (ValueErrorException e) {
      System.out.print("\033[1B\r\033[2K\033[1A");
    }
  }

  private static String readLine() throws IOException {

    System.out.print(renderer.getPrompt());
    String out = "";

    while (true) {

      renderer.render();

      Key key = terminal.read();

      if (key.type() == KeyType.ENTER) {

        if (state.buffer.isEmpty()) {
          continue;
        }

        out = state.buffer.toString().trim();
        state.buffer.setLength(0);
        state.cursor = 0;
        System.out.print("\r\033[2K" + renderer.getPrompt() + out);

        break;
      }

      var cmd = state.mapper.map(key);

      if (cmd == null)
        continue;

      cmd.execute(state);

      preview(state.buffer.toString());

    }

    System.out.println();
    return out;
  }

  private static void exit(int exitStatus, String messsage) {
    try {
      state.effects.clearLine();
      state.effects.showCursor();
      terminal.restore();

      if (!messsage.isEmpty())
        System.out.println(messsage);

      System.exit(exitStatus);
    } catch (Exception e) {
    }
  }

}
