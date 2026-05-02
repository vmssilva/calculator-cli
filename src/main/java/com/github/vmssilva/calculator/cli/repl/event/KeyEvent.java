package com.github.vmssilva.calculator.cli.repl.event;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.command.Command;

public interface KeyEvent {
  void onKey(Key key, Command command);

}
