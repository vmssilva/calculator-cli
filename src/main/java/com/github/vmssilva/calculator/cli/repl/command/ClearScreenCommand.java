package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class ClearScreenCommand implements Command {

  @Override
  public void execute(State state) {
    state.effects.clearScreen();
  }

}
