package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.IOException;
import java.io.InputStream;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.KeyType;

public class TerminalControl {

  private final Terminal terminal;

  public TerminalControl(Terminal terminal) {
    this.terminal = terminal;
  }

  public void init() throws IOException, InterruptedException {
    terminal.setup();
  }

  public void restore() throws IOException, InterruptedException {
    terminal.restore();
  }

  public Key read() throws IOException {
    InputStream in = terminal.in();
    int ch = in.read();

    if (ch == -1) {
      return new Key("", KeyType.UNKNOWN);
    }

    // ENTER
    if (ch == '\n' || ch == '\r') {
      return new Key("ENTER", KeyType.ENTER);
    }

    // TAB
    if (ch == '\t') {
      return new Key("TAB", KeyType.TAB);
    }

    // BACKSPACE
    if (ch == 127 || ch == 8) {
      return new Key("BACKSPACE", KeyType.BACKSPACE);
    }

    // SPACE
    if (ch == ' ') {
      return new Key(" ", KeyType.SPACE);
    }

    // ESC / ANSI / ALT
    if (ch == 27) {
      return readEscape(in);
    }

    // CTRL (ASCII < 32)
    if (ch < 32) {
      char ctrl = (char) (ch + 64);
      return new Key("CTRL+" + ctrl, KeyType.CTRL);
    }

    // SHIFT (heurística)
    if (Character.isUpperCase(ch)) {
      return new Key(String.valueOf((char) ch), KeyType.REGULAR);
    }

    return new Key(String.valueOf((char) ch), KeyType.REGULAR);
  }

  private Key readEscape(InputStream in) throws IOException {
    if (in.available() <= 0) {
      return new Key("ESC", KeyType.ESC);
    }

    int next = in.read();

    // ALT + tecla
    if (next != '[' && next != 'O') {
      return new Key(String.valueOf((char) next), KeyType.ALT);
    }

    // ESC O (F1–F4, alguns terminais)
    if (next == 'O') {
      int code = in.read();
      return switch (code) {
        case 'P' -> new Key("F1", KeyType.FUNCTION);
        case 'Q' -> new Key("F2", KeyType.FUNCTION);
        case 'R' -> new Key("F3", KeyType.FUNCTION);
        case 'S' -> new Key("F4", KeyType.FUNCTION);
        case 'H' -> new Key("HOME", KeyType.HOME);
        case 'F' -> new Key("END", KeyType.END);
        default -> new Key("ESC_O_" + (char) code, KeyType.UNKNOWN);
      };
    }

    // ESC [
    int code = in.read();

    if (Character.isLetter(code)) {
      return mapLetter(code);
    }

    // ESC [ number ~
    if (Character.isDigit(code)) {
      StringBuilder seq = new StringBuilder();
      seq.append((char) code);

      while (true) {
        int c = in.read();

        if (c == -1) {
          return new Key("ESC_SEQ", KeyType.UNKNOWN);
        }

        if (c == 'R') {
          seq.append((char) c);
          return new Key("\033[" + seq.toString(), KeyType.ANSI);
        }

        if (c == '~') {
          seq.append((char) c);
          break;
        }

        seq.append((char) c);
      }

      String number = seq.substring(0, seq.length() - 1);

      int n;
      try {
        n = Integer.parseInt(number);
      } catch (NumberFormatException e) {
        return new Key("ESC_SEQ", KeyType.UNKNOWN);
      }

      return switch (n) {
        case 1, 7 -> new Key("HOME", KeyType.HOME);
        case 2 -> new Key("INSERT", KeyType.INSERT);
        case 3 -> new Key("DELETE", KeyType.DELETE);
        case 4, 8 -> new Key("END", KeyType.END);
        case 5 -> new Key("PAGE_UP", KeyType.PAGE_UP);
        case 6 -> new Key("PAGE_DOWN", KeyType.PAGE_DOWN);

        case 11 -> new Key("F1", KeyType.FUNCTION);
        case 12 -> new Key("F2", KeyType.FUNCTION);
        case 13 -> new Key("F3", KeyType.FUNCTION);
        case 14 -> new Key("F4", KeyType.FUNCTION);
        case 15 -> new Key("F5", KeyType.FUNCTION);
        case 17 -> new Key("F6", KeyType.FUNCTION);
        case 18 -> new Key("F7", KeyType.FUNCTION);
        case 19 -> new Key("F8", KeyType.FUNCTION);
        case 20 -> new Key("F9", KeyType.FUNCTION);
        case 21 -> new Key("F10", KeyType.FUNCTION);
        case 23 -> new Key("F11", KeyType.FUNCTION);
        case 24 -> new Key("F12", KeyType.FUNCTION);

        default -> new Key("ESC[" + number + "~", KeyType.UNKNOWN);
      };
    }

    return new Key("ESC_SEQ", KeyType.UNKNOWN);
  }

  private Key mapLetter(int code) {
    return switch (code) {
      case 'A' -> new Key("UP", KeyType.UP);
      case 'B' -> new Key("DOWN", KeyType.DOWN);
      case 'C' -> new Key("RIGHT", KeyType.RIGHT);
      case 'D' -> new Key("LEFT", KeyType.LEFT);
      case 'H' -> new Key("HOME", KeyType.HOME);
      case 'F' -> new Key("END", KeyType.END);
      default -> new Key("MOD_" + (char) code, KeyType.UNKNOWN);
    };
  }
}
