package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnixTerminal implements Terminal, TerminalOutput {

  private String settings;
  private InputStream in;

  @Override
  public void setup() throws IOException, InterruptedException {
    var gsettings = new String[] { "sh", "-c", "stty -g < /dev/tty" };
    var out = Runtime.getRuntime()
        .exec(gsettings)
        .getInputStream()
        .readAllBytes();

    settings = new String(out).trim();

    var raw = new String[] { "sh", "-c", "stty raw -echo < /dev/tty" };
    Runtime.getRuntime().exec(raw).waitFor();

    this.in = new FileInputStream("/dev/tty");
  }

  @Override
  public void restore() throws IOException, InterruptedException {
    var restore = new String[] { "sh", "-c", "stty " + settings + " < /dev/tty" };
    Runtime.getRuntime().exec(restore).waitFor();
  }

  @Override
  public InputStream in() {
    return in;
  }

  // =========================
  // TERMINAL OUTPUT (ANSI)
  // =========================

  @Override
  public TerminalOutput write(String s) {
    System.out.print(s);
    System.out.flush();
    return this;
  }

  @Override
  public TerminalOutput flush() {
    System.out.flush();
    return this;
  }

  @Override
  public TerminalOutput clearScreen() {
    write("\033[2J\033[H");
    return this;
  }

  @Override
  public TerminalOutput clearLine() {
    write("\r\033[2K");
    return this;
  }

  @Override
  public TerminalOutput moveCursor(int row, int col) {
    write("\033[" + row + ";" + col + "H");
    return this;
  }

  @Override
  public TerminalOutput hideCursor() {
    write("\033[?25l");
    return this;
  }

  @Override
  public TerminalOutput showCursor() {
    write("\033[?25h");
    return this;
  }

  @Override
  public TerminalOutput saveCursor() {
    write("\0337");
    return this;
  }

  @Override
  public TerminalOutput restoreCursor() {
    write("\0338");
    return this;
  }

  public TerminalOutput moveCursorRelative(int deltaRow, int deltaCol) {

    if (deltaRow < 0) {
      System.out.print("\033[" + (-deltaRow) + "A"); // up
    } else if (deltaRow > 0) {
      System.out.print("\033[" + deltaRow + "B"); // down
    }

    if (deltaCol < 0) {
      System.out.print("\033[" + (-deltaCol) + "D"); // left
    } else if (deltaCol > 0) {
      System.out.print("\033[" + deltaCol + "C"); // right
    }

    return this;
  }

}
