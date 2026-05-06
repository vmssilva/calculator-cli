package com.github.vmssilva.calculator.cli.repl.command;

public class RightCommand implements Command {

  @Override
  public void execute(CommandContext context) {
    if (context.state().cursorX < context.state().buffer.length())
      context.state().cursorX++;
  }
}
