package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.IOException;
import java.io.InputStream;

public class TerminalInfo {

  // =========================
  // SIZE (cached)
  // =========================
  private static int[] sizeCache;
  private static long sizeLastUpdate = 0;
  private static final long SIZE_CACHE_MS = 1000;

  public static int[] size() {
    long now = System.currentTimeMillis();

    if (sizeCache != null && (now - sizeLastUpdate) < SIZE_CACHE_MS) {
      return sizeCache;
    }

    try {
      Process p = new ProcessBuilder(
          "sh", "-c", "stty size < /dev/tty").start();

      String out = new String(p.getInputStream().readAllBytes()).trim();
      p.waitFor();

      String[] parts = out.split("\\s+");

      if (parts.length != 2) {
        return fallbackSize();
      }

      sizeCache = new int[] {
          Integer.parseInt(parts[0]),
          Integer.parseInt(parts[1])
      };

      sizeLastUpdate = now;

      return sizeCache;

    } catch (Exception e) {
      return fallbackSize();
    }
  }

  private static int[] fallbackSize() {
    return new int[] { 24, 80 };
  }

  // =========================
  // CURSOR POSITION (ANSI)
  // =========================
  public static int[] cursorPosition(InputStream in) throws IOException {
    System.out.print("\033[6n");
    System.out.flush();

    StringBuilder buffer = new StringBuilder();

    int ch;

    while ((ch = in.read()) != 27) {
      if (ch == -1) {
        throw new IOException("EOF before ESC");
      }
    }

    buffer.append((char) ch);

    ch = in.read();
    if (ch != '[') {
      throw new IOException("Invalid ANSI response");
    }

    buffer.append((char) ch);

    while ((ch = in.read()) != 'R') {
      if (ch == -1) {
        throw new IOException("Incomplete ANSI response");
      }
      buffer.append((char) ch);
    }

    buffer.append('R');

    return parseCursor(buffer.toString());
  }

  private static int[] parseCursor(String ansi) {
    String body = ansi.substring(2, ansi.length() - 1);
    String[] parts = body.split(";");

    return new int[] {
        Integer.parseInt(parts[0]),
        Integer.parseInt(parts[1])
    };
  }
}
