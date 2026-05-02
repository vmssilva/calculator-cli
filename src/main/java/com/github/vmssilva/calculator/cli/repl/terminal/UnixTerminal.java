package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnixTerminal implements Terminal {

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
}
