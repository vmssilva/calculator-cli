package com.github.vmssilva.calculator.cli.repl.command;

public class ClearScreenCommand implements Command {

  @Override
  public void execute(CommandContext context) {
    context.out().clearScreen();
  }

}
