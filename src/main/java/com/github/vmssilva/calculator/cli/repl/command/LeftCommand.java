
package com.github.vmssilva.calculator.cli.repl.command;

public class LeftCommand implements Command {

  @Override
  public void execute(CommandContext context) {
    if (context.state().cursorX > 0)
      context.state().cursorX--;
  }
}
