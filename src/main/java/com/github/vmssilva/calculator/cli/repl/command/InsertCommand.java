package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.state.State;

public class InsertCommand implements Command {

  private final Key key;

  public InsertCommand(Key key) {
    this.key = key;
  }

  @Override
  public void execute(State state) {
    state.buffer.insert(state.cursorX, key.value());
    state.cursorX++;
  }
}
