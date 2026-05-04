package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class ClearLeftCommand implements Command {
  public void execute(State s) {
    s.buffer.delete(0, s.cursorX);
    s.cursorX = 0;
  }
}
