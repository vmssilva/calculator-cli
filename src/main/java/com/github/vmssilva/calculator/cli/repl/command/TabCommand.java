package com.github.vmssilva.calculator.cli.repl.command;

public class TabCommand implements Command {

  @Override
  public void execute(CommandContext context) {

    var value = context.state().buffer.toString();

    if (!(value.startsWith("!")))
      return;

    try {
      int index = Integer.valueOf(value.substring(1));

      if (index < 0 || index >= context.state().history.size())
        return;

      context.state().buffer.setLength(0);
      context.state().buffer.append(context.state().history.get(index));
      context.state().cursorX = context.state().buffer.length();

    } catch (NumberFormatException e) {
      return;
    }
  }

}
