package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class HomeCommand implements Command {

  @Override
  public void execute(State state) {
    state.cursor = 0;
  }
}
