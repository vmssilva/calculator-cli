package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class BackspaceCommand implements Command {

  @Override
  public void execute(State state) {
    if (state.cursorX == 0)
      return;

    state.buffer.deleteCharAt(state.cursorX - 1);
    state.cursorX--;
  }
}
