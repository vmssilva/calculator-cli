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
import com.github.vmssilva.calculator.engine.exception.CalculatorLexerException;
import com.github.vmssilva.calculator.engine.exception.CalculatorParserException;
import com.github.vmssilva.calculator.engine.exception.ExecutionErrorException;
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
      default -> throw new UnsupportedOperationException("Invalid option: '" + cmd + "'");
    }
  }

  private static Value evaluate(String expression, ApplicationContext context) {
    try {
      return runtime.evaluate(expression, context);
    } catch (ValueErrorException | ExecutionErrorException | CalculatorLexerException | CalculatorParserException e) {
      throw e;
    } catch (Exception rt) {
      throw new RuntimeException("Unknown error");
    }
  }

  private static void repl() {

    state.mapper.register(List.of("CTRL+C", "CTRL+D"), (s) -> {
      exit(1, "");
    });

    try {
      terminal.init();
      state.effects.hideCursor();
      String input = "";

      while (true) {

        state.effects.write(renderer.getPrompt());

        input = getLine();

        state.effects.write(Ansi.clearLine() + renderer.getPrompt() + input + "\n");

        if (input.startsWith("\\")) {
          try {
            handleCommand(input.substring(1));
          } catch (Exception e) {
            state.effects.write(Ansi.clearLine() + e.getMessage() + "\n");
            continue;
          }
        }

        try {
          var result = evaluate(input, context);
          state.history.add(input);
          state.effects.write("\r" + result.toString() + "\n");
        } catch (Exception e) {
          state.effects.write("\r" + e.getMessage() + "\n");
          continue;
        }
      }
    } catch (Exception e) {
    } finally {
      state.effects.showCursor();
      try {
        terminal.restore();
      } catch (IOException | InterruptedException e) {
        state.effects.write("\n" + "Error when trying to restore terminal configurations" + "\n");
        System.exit(1);
      }
    }
  }

  private static void preview(String pre) {

    if (pre.isEmpty()) {
      state.effects.write(Ansi.cursorDown(1) + Ansi.clearLine() + Ansi.cursorUp(1));
      return;
    }

    try {

      state.effects
          .write(Ansi.cursorDown(1) + Ansi.clearLine() + "\033[90m" + evaluate(pre, context).toString() + Ansi.RESET
              + Ansi.cursorUp(1));

    } catch (Exception e) {
      state.effects.write(Ansi.cursorDown(1) + Ansi.clearLine() + Ansi.cursorUp(1));
    }
  }

  private static String getLine() throws IOException {

    String output = "";
    Command command = null;

    while (true) {

      renderer.render();

      Key key = terminal.read();

      if (key.type() == KeyType.ENTER) {

        if (state.buffer.isEmpty()) {
          continue;
        }

        output = state.buffer.toString().trim();
        state.buffer.setLength(0);
        state.cursor = 0;

        break;
      }

      command = state.mapper.map(key);

      if (command == null)
        continue;

      command.execute(state);

      preview(state.buffer.toString());

    }

    return output;
  }

  private static void exit(int exitStatus, String messsage) {
    try {
      state.effects.clearLine();
      state.effects.showCursor();
      terminal.restore();

      if (!messsage.isEmpty())
        state.effects.write(messsage + "\n");

      System.exit(exitStatus);
    } catch (Exception e) {
      state.effects.write("\n" + "Error when trying to restore terminal configurations" + "\n");
    }
  }

}
