package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.Key;

public class InsertCommand implements Command {

  private final Key key;

  public InsertCommand(Key key) {
    this.key = key;
  }

  @Override
  public void execute(CommandContext context) {
    context.state().buffer.insert(context.state().cursorX, key.value());
    context.state().cursorX++;
  }
}
