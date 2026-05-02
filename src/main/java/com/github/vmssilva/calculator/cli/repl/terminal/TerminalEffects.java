package com.github.vmssilva.calculator.cli.repl.terminal;

public class TerminalEffects implements Effects {

  @Override
  public void write(String s) {
    System.out.print(s);
    System.out.flush();
  }

  @Override
  public void clearScreen() {
    System.out.print("\033[2J\033[H");
  }

  @Override
  public void showCursor() {
    System.out.print("\033[?25h");
  }

  @Override
  public void hideCursor() {
    System.out.print("\033[?25l");
  }

  @Override
  public void clearLine() {
    System.out.print("\r\033[2K"); // limpa linha atual (input)
  }
}
