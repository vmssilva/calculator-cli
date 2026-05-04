package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class EnterCommand implements Command {
  @Override
  public void execute(State state) {
    state.buffer.setLength(0);
    state.historyIndex = -1;
    state.cursorX = 0;
  }
}
