package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class DownCommand implements Command {

  @Override
  public void execute(State state) {

    if (state.historyIndex == -1)
      return;

    if (state.historyIndex < state.history.size() - 1) {
      state.historyIndex++;
      state.buffer.setLength(0);
      state.buffer.append(state.history.get(state.historyIndex));
      state.cursorX = state.buffer.length();
    } else {
      state.historyIndex = -1;

      state.buffer.setLength(0);
      state.buffer.append(state.savedBuffer);
      state.cursorX = state.buffer.length();
    }
  }

}
