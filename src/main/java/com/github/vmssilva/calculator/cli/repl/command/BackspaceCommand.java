package com.github.vmssilva.calculator.cli.repl.command;

public class BackspaceCommand implements Command {

  @Override
  public void execute(CommandContext context)  {
    if (context.state().cursorX == 0)
      return;

    context.state().buffer.deleteCharAt(context.state().cursorX - 1);
    context.state().cursorX--;
  }
}
