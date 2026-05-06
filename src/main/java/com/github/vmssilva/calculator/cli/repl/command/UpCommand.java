package com.github.vmssilva.calculator.cli.repl.command;

public class UpCommand implements Command {

  @Override
  public void execute(CommandContext context) {

    if (context.state().history.size() <= 0)
      return;

    if (context.state().historyIndex == -1) {
      context.state().savedBuffer = context.state().buffer.toString();
      context.state().historyIndex = context.state().history.size() - 1;
    } else {
      if (context.state().historyIndex > 0) {
        context.state().historyIndex--;
      }
    }

    context.state().buffer.setLength(0);
    context.state().buffer.append(context.state().history.get(context.state().historyIndex));
    context.state().cursorX = context.state().buffer.length();
  }

}
