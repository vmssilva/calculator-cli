package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class EndCommand implements Command {

  @Override
  public void execute(State state) {
    state.cursorX = state.buffer.length();
  }
}
