package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class UpCommand implements Command {

  @Override
  public void execute(State state) {

    if (state.history.size() <= 0)
      return;

    if (state.historyIndex == -1) {
      state.savedBuffer = state.buffer.toString();
      state.historyIndex = state.history.size() - 1;
    } else {
      if (state.historyIndex > 0) {
        state.historyIndex--;
      }
    }

    state.buffer.setLength(0);
    state.buffer.append(state.history.get(state.historyIndex));
    state.cursorX = state.buffer.length();
  }

}
