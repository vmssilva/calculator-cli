package com.github.vmssilva.calculator.cli.repl.command;

public class EnterCommand implements Command {
  @Override
  public void execute(CommandContext context) {
    context.state().buffer.setLength(0);
    context.state().historyIndex = -1;
    context.state().cursorX = 0;
  }
}
