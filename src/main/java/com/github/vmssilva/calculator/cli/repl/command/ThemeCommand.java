package com.github.vmssilva.calculator.cli.repl.command;

import com.github.vmssilva.calculator.cli.repl.style.AsciiTheme;
import com.github.vmssilva.calculator.cli.repl.style.DefaultTheme;

public class ThemeCommand implements Command {

  private final String arg;

  public ThemeCommand(String arg) {
    this.arg = arg;
  }

  @Override
  public void execute(CommandContext context) {

    switch (arg) {
      case "ascii" -> context.state().setTheme(new AsciiTheme());
      case "unicode", "default" -> context.state().setTheme(new DefaultTheme());
      default -> context.out()
          .write("Unknown theme: " + arg + "\n")
          .flush();
    }
  }
}
