package com.github.vmssilva.calculator.cli.helper;

public final class Ansi {

  public static final String CLEAR_LINE = "\033[2K";
  public static final String CURSOR_HIDE = "\033[?25l";
  public static final String CURSOR_SHOW = "\033[?25h";
  public static final String RESET = "\033[0m";
  public static final String INVERT = "\033[7m";

  public static final Color Color = new Color();

  public static String clearLine() {
    return "\r" + CLEAR_LINE;
  }

  public static String cursorDown(int index) {
    return "\033[" + index + "B";
  }

  public static String cursorUp(int index) {
    return "\033[" + index + "A";
  }

  public static String cursorLeft(int index) {
    return "\033[" + index + "D";
  }

  public static String cursorRight(int index) {
    return "\033[" + index + "C";
  }

  public static String saveCursor() {
    return "\0337";
  }

  public static String restoreCursor() {
    return "\0338";
  }

  public static class Color {
    public String GRAY = "\033[90m";
  }
}
