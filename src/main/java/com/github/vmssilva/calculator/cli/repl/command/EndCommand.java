package com.github.vmssilva.calculator.cli.repl.command;

public class EndCommand implements Command {

  @Override
  public void execute(CommandContext context) {
    context.state().cursorX = context.state().buffer.length();
  }
}
