package com.github.vmssilva.calculator.cli.repl;

import java.util.function.Function;

import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.style.Color;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalOutput;

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
      out.write(state.theme.prompt());
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

    out.write(state.theme.prompt());
    out.write(rendered.toString());

  }

  public void renderPreview(String eval, Function<String, String> evaluator, int row, int col) {
    String evaluated = evaluator.apply(eval);

    if (evaluated.isEmpty()) {
      out.saveCursor()
          .moveCursor(row, col)
          .write("\r")
          .clearLine()
          .restoreCursor()
          .flush();

      return;
    }

    out.saveCursor()
        .moveCursor(row, col)
        .write("\r")
        .clearLine()
        .setColor(state.theme.previewColor())
        .write(state.theme.previewArrow())
        .write(" ")
        .write(evaluated)
        .setColor(Color.RESET)
        .restoreCursor()
        .flush();

  }

  public String getPrompt() {
    return state.theme.prompt();
  }
}
