package com.github.vmssilva.calculator.cli.repl.command;

public class DownCommand implements Command {

  @Override
  public void execute(CommandContext context) {

    if (context.state().historyIndex == -1)
      return;

    if (context.state().historyIndex < context.state().history.size() - 1) {
      context.state().historyIndex++;
      context.state().buffer.setLength(0);
      context.state().buffer.append(context.state().history.get(context.state().historyIndex));
      context.state().cursorX = context.state().buffer.length();
    } else {
      context.state().historyIndex = -1;

      context.state().buffer.setLength(0);
      context.state().buffer.append(context.state().savedBuffer);
      context.state().cursorX = context.state().buffer.length();
    }
  }

}
