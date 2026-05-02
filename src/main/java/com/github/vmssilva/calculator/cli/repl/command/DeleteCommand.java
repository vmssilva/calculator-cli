package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class DeleteCommand implements Command {

  @Override
  public void execute(State state) {

    if (state.cursor == state.buffer.length())
      return;

    state.buffer.deleteCharAt(state.cursor);

  }
}
