package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.InputStream;

public class TerminalSession {

  private final Terminal terminal;
  private final TerminalOutput output;
  private final TerminalControl control;

  public TerminalSession(Terminal terminal) {
    this(terminal, (TerminalOutput) terminal);
  }

  public TerminalSession(Terminal terminal, TerminalOutput output) {
    this.terminal = terminal;
    this.output = output;
    this.control = new TerminalControl(terminal);
  }

  // =========================
  // ACCESSORS
  // =========================

  public Terminal terminal() {
    return terminal;
  }

  public TerminalOutput out() {
    return output;
  }

  public TerminalControl in() {
    return control;
  }

  // =========================
  // SHORTCUTS (opcional, conveniência)
  // =========================

  public int[] size() {
    return TerminalInfo.size();
  }

  public int[] cursorPosition(InputStream in) throws Exception {
    return TerminalInfo.cursorPosition(in);
  }

  // =========================
  // LIFECYCLE
  // =========================

  public void start() throws Exception {
    terminal.setup();
  }

  public void stop() throws Exception {
    terminal.restore();
  }
}
