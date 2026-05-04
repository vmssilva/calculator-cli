package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class ClearRightCommand implements Command {

  @Override
  public void execute(State state) {
    state.buffer.delete(state.cursorX, state.buffer.length());
  }

}
