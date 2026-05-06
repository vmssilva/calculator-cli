package com.github.vmssilva.calculator.cli.repl.command;

public class ClearLeftCommand implements Command {
  public void execute(CommandContext context) {
    context.state().buffer.delete(0, context.state().cursorX);
    context.state().cursorX = 0;
  }
}
