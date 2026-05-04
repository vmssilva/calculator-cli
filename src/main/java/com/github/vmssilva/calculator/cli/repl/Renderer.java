package com.github.vmssilva.calculator.cli.repl;

import com.github.vmssilva.calculator.cli.helper.Ansi;
import com.github.vmssilva.calculator.cli.repl.state.State;

public class Renderer {

  private State state;
  private final String PROMPT;

  public Renderer(State state, String prompt) {
    this.state = state;
    this.PROMPT = prompt;
  }

  public void render() {

    StringBuilder out = new StringBuilder();

    if (state.buffer.isEmpty()) {
      System.out.print("\033[2K\r" + PROMPT + Ansi.INVERT + " " + Ansi.RESET);
      return;
    }

    for (int i = 0; i < state.buffer.length(); i++) {
      if (i == state.cursorX) {
        out.append(Ansi.INVERT + state.buffer.charAt(i) + Ansi.RESET);
      } else {
        out.append(state.buffer.charAt(i));
      }
    }

    if (state.cursorX >= out.length()) {
      out.append(Ansi.INVERT + " " + Ansi.RESET);
    }

    System.out.print("\r\033[2K" + PROMPT + out.toString());

  }

  public String getPrompt() {
    return PROMPT;
  }
}
