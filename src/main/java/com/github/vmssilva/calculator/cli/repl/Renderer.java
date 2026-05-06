package com.github.vmssilva.calculator.cli.repl;

import java.util.function.Function;

import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.style.Color;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalOutput;

public class Renderer {

  private final State state;
  private final String prompt;
  private final TerminalOutput out;

  public Renderer(State state, String prompt, TerminalOutput out) {
    this.state = state;
    this.prompt = prompt;
    this.out = out;
  }

  public void render() {

    out.clearLine();
    out.write("\r").clearLine();

    if (state.buffer.isEmpty()) {
      out.write(prompt);
      out.setColor(Color.INVERT);
      out.write(" ");
      out.setColor(Color.RESET);
      return;
    }

    StringBuilder rendered = new StringBuilder();

    for (int i = 0; i < state.buffer.length(); i++) {
      char c = state.buffer.charAt(i);

      if (i == state.cursorX) {
        rendered.append(Color.INVERT)
            .append(c)
            .append(Color.RESET);
      } else {
        rendered.append(c);
      }
    }

    if (state.cursorX >= state.buffer.length()) {
      rendered.append(Color.INVERT)
          .append(" ")
          .append(Color.RESET);
    }

    out.write(prompt);
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
        .setColor(Color.GRAY)
        .write("→ ")
        .write(evaluated)
        .setColor(Color.RESET)
        .restoreCursor()
        .flush();

  }

  public String getPrompt() {
    return prompt;
  }
}
