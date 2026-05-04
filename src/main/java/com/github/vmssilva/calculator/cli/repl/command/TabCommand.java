package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.state.State;

public class TabCommand implements Command {

  @Override
  public void execute(State state) {

    var value = state.buffer.toString();

    if (!(value.startsWith("!")))
      return;

    try {
      int index = Integer.valueOf(value.substring(1));

      if (index < 0 || index >= state.history.size())
        return;

      state.buffer.setLength(0);
      state.buffer.append(state.history.get(index));
      state.cursorX = state.buffer.length();

    } catch (NumberFormatException e) {
      return;
    }
  }

}
