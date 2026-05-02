package com.github.vmssilva.calculator.cli.repl.terminal;

public interface Effects {
  void write(String s);

  void clearScreen();

  void clearLine();

  void showCursor();

  void hideCursor();
}
