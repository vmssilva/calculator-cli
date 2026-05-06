package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalOutput;

public class CommandContext {

  private final TerminalOutput out;
  private final State state;

  public CommandContext(State state, TerminalOutput out) {
    this.state = state;
    this.out = out;
  }

  public State state() {
    return state;
  }

  public TerminalOutput out() {
    return out;
  }
}
