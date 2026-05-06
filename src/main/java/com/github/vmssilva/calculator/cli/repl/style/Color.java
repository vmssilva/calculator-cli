package com.github.vmssilva.calculator.cli.repl.style;

public enum Color {

  RESET("\033[0m"),

  // ===== foreground =====
  BLACK("\033[30m"),
  RED("\033[31m"),
  GREEN("\033[32m"),
  YELLOW("\033[33m"),
  BLUE("\033[34m"),
  MAGENTA("\033[35m"),
  CYAN("\033[36m"),
  WHITE("\033[37m"),

  // ===== bright =====
  BRIGHT_BLACK("\033[90m"),
  BRIGHT_RED("\033[91m"),
  BRIGHT_GREEN("\033[92m"),
  BRIGHT_YELLOW("\033[93m"),
  BRIGHT_BLUE("\033[94m"),
  BRIGHT_MAGENTA("\033[95m"),
  BRIGHT_CYAN("\033[96m"),
  BRIGHT_WHITE("\033[97m"),

  // ===== styles =====
  BOLD("\033[1m"),
  DIM("\033[2m"),
  ITALIC("\033[3m"),
  UNDERLINE("\033[4m"),
  BLINK("\033[5m"),
  REVERSE("\033[7m"), // invert
  HIDDEN("\033[8m"),

  // alias útil
  INVERT("\033[7m");

  private final String ansi;

  Color(String ansi) {
    this.ansi = ansi;
  }

  public String ansi() {
    return ansi;
  }

  @Override
  public String toString() {
    return ansi;
  }
}
