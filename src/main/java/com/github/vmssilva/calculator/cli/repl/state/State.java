package com.github.vmssilva.calculator.cli.repl.state;

import com.github.vmssilva.calculator.cli.repl.History;
import com.github.vmssilva.calculator.cli.repl.mapper.KeyMapper;
import com.github.vmssilva.calculator.cli.repl.terminal.Effects;
import com.github.vmssilva.calculator.cli.repl.terminal.TerminalEffects;

public class State {

  public final Effects effects = new TerminalEffects();
  public final KeyMapper mapper = new KeyMapper();
  public final StringBuffer buffer = new StringBuffer();
  public String savedBuffer = "";
  public final History history = new History();
  public int historyIndex = -1;

  public int cursor = 0;

}
