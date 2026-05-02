package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public interface Command {
  void execute(State state);
}
