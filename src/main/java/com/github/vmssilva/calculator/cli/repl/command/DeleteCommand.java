package com.github.vmssilva.calculator.cli.repl.command;

public class DeleteCommand implements Command {

  @Override
  public void execute(CommandContext context) {

    if (context.state().cursorX == context.state().buffer.length())
      return;

    context.state().buffer.deleteCharAt(context.state().cursorX);

  }
}
