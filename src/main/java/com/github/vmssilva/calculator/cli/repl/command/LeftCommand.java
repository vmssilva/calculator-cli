
package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class LeftCommand implements Command {

  @Override
  public void execute(State state) {
    if (state.cursor > 0)
      state.cursor--;
  }
}
