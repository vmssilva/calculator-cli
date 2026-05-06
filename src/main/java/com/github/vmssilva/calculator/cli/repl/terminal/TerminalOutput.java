package com.github.vmssilva.calculator.cli.repl.terminal;

public interface TerminalOutput {

  // =========================
  // SCREEN CONTROL
  // =========================

  TerminalOutput clearScreen();

  TerminalOutput clearLine();

  // =========================
  // CURSOR CONTROL
  // =========================

  TerminalOutput moveCursor(int row, int col);

  TerminalOutput hideCursor();

  TerminalOutput showCursor();

  TerminalOutput saveCursor();

  TerminalOutput restoreCursor();

  TerminalOutput moveCursorRelative(int deltaRow, int deltaCol);

  TerminalOutput write(String s);

  TerminalOutput flush();

  default TerminalOutput setColor(String color) {
    write(color);
    return this;
  }

  // =========================
  // OPTIONAL (EXTENSÃO FUTURA)
  // =========================

  default TerminalOutput reset() {
    clearScreen();
    moveCursor(1, 1);
    showCursor();
    write("\033[0m");
    return this;
  }

  default TerminalOutput moveUp(int n) {
    moveCursorRelative(-n, 0);
    return this;
    // write("\033[" + n + "A");
  }

  default TerminalOutput moveDown(int n) {
    // write("\033[" + n + "B");
    return moveCursorRelative(n, 0);

  }

  default TerminalOutput moveLeft(int n) {
    return moveCursorRelative(0, -n);
    // write("\033[" + n + "D");
  }

  default TerminalOutput moveRight(int n) {
    return moveCursorRelative(0, n);
    // write("\033[" + n + "C");
  }

}
