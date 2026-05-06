package com.github.vmssilva.calculator.cli.repl.command;

public class HomeCommand implements Command {

  @Override
  public void execute(CommandContext context) {
    context.state().cursorX = 0;
  }
}
