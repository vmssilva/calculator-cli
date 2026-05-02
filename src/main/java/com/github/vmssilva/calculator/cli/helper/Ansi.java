package com.github.vmssilva.calculator.cli.helper;

public final class Ansi {

  public static final String CLEAR_LINE = "\033[2K";
  public static final String CURSOR_HIDE = "\033[?25l";
  public static final String CURSOR_SHOW = "\033[?25h";
  public static final String RESET = "\033[0m";
  public static final String INVERT = "\033[7m";

  public static String clearLine() {
    return "\r" + CLEAR_LINE;
  }
}
