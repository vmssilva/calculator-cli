package com.github.vmssilva.calculator.cli.repl;

import java.util.function.Function;

import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.style.Color;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalOutput;
import com.github.vmssilva.calculator.engine.std.value.FunctionValue;
import com.github.vmssilva.calculator.engine.std.value.ListValue;
import com.github.vmssilva.calculator.engine.std.value.StringValue;
import com.github.vmssilva.calculator.engine.std.value.Value;

public class Renderer {

  private final State state;
  private final TerminalOutput out;

  public Renderer(State state, TerminalOutput out) {
    this.state = state;
    this.out = out;
  }

  public void render() {

    out.clearLine();
    out.write("\r").clearLine();

    var cursor = state.theme.cursor();

    if (state.buffer.isEmpty()) {

      // out.write(state.theme.prompt());
      out.write(getPrompt());
      out.setColor(state.theme.cursorColor());
      out.write(cursor.isEmpty() ? " " : cursor);
      out.setColor(Color.RESET);
      return;
    }

    StringBuilder rendered = new StringBuilder();

    for (int i = 0; i < state.buffer.length(); i++) {
      char c = state.buffer.charAt(i);

      if (i == state.cursorX) {
        rendered.append(state.theme.cursorColor());

        if (cursor.isEmpty()) {
          rendered.append(c);
          rendered.append(Color.RESET);
        } else {
          rendered.append(cursor);
          rendered.append(Color.RESET);
          rendered.append(c);
        }
      } else {
        rendered.append(c);
      }
    }

    if (state.cursorX >= state.buffer.length()) {
      rendered.append(state.theme.cursorColor());
      rendered.append((cursor.isEmpty()) ? " " : cursor);
      rendered.append(Color.RESET);
    }

    out.write(getPrompt());
    out.write(rendered.toString());

  }

  public void renderPreview(String eval, Function<String, Value<?>> evaluator, int row,
      int col) {

    Value<?> evaluated = evaluator.apply(eval);

    if (evaluated instanceof StringValue || evaluated instanceof ListValue) {

      out.saveCursor()
          .moveCursor(row, col)
          .write("\r")
          .clearLine()
          .restoreCursor()
          .flush();

      return;
    }

    String value = evaluated.unwrap().toString();

    if (evaluated instanceof FunctionValue fn) {
      value = fn.toString();
    }

    out.saveCursor()
        .moveCursor(row, col)
        .write("\r")
        .clearLine()
        .setColor(state.theme.previewColor())
        .write(state.theme.previewArrow())
        .write(" ")
        .write(value)
        .setColor(Color.RESET)
        .restoreCursor()
        .flush();

  }

  public String getPrompt() {

    var symbol = state.theme.successSymbol();
    var symbolColor = state.theme.successColor();

    if (state.isError) {
      symbol = state.theme.errorSymbol();
      symbolColor = state.theme.errorColor();
    }

    return symbolColor.ansi() + (symbol.isEmpty() ? "" : symbol + " ") + Color.RESET.ansi() + state.theme.prompt();
  }
}
