package com.github.vmssilva.calculator.cli.repl.state;

import com.github.vmssilva.calculator.cli.repl.History;
import com.github.vmssilva.calculator.cli.repl.mapper.KeyMapper;
import com.github.vmssilva.calculator.cli.repl.terminal.Effects;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalEffects;

public class State {

  public final Effects effects;
  public final KeyMapper mapper;
  public final History history;

  // input atual
  public final StringBuilder buffer = new StringBuilder();

  // posição do cursor (apenas eixo X, já que é single-line)
  public int cursorX = 0;

  // histórico
  public int historyIndex = -1;
  public String savedBuffer = "";

  public State() {
    this.effects = new TerminalEffects();
    this.history = new History();
    this.mapper = new KeyMapper();
  }

  public void resetInput() {
    buffer.setLength(0);
    cursorX = 0;
    historyIndex = -1;
    savedBuffer = "";
  }
}

// package com.github.vmssilva.calculator.cli.repl.state;
//
// import com.github.vmssilva.calculator.cli.repl.History;
// import com.github.vmssilva.calculator.cli.repl.Key;
// import com.github.vmssilva.calculator.cli.repl.mapper.KeyMapper;
// import com.github.vmssilva.calculator.cli.repl.terminal.Effects;
// import com.github.vmssilva.calculator.cli.repl.terminal.TerminalEffects;
//
// public class State {
//
// public final Effects effects = new TerminalEffects();
// public final KeyMapper mapper = new KeyMapper(this);
// public final StringBuffer buffer = new StringBuffer();
// public String savedBuffer = "";
// public final History history = new History();
// public int historyIndex = -1;
// public Key key = null;
// public Cursor cursor = new Cursor();
//
// public static class Cursor {
// public int x = 0;
// public int y = 0;
// }
//
// }
