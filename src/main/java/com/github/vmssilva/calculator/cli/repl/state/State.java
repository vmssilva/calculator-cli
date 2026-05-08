package com.github.vmssilva.calculator.cli.repl.state;

import com.github.vmssilva.calculator.cli.repl.History;
import com.github.vmssilva.calculator.cli.repl.mapper.KeyMapper;
import com.github.vmssilva.calculator.cli.repl.style.DefaultTheme;
import com.github.vmssilva.calculator.cli.repl.style.Theme;

public class State {

  public final KeyMapper mapper;
  public History history;
  public final StringBuilder buffer = new StringBuilder();
  public Theme theme = new DefaultTheme();

  public int cursorX = 0;
  public int historyIndex = -1;
  public String savedBuffer = "";
  public boolean isError = false;

  public State() {
    this.history = new History();
    this.mapper = new KeyMapper();
  }

  public void resetInput() {
    buffer.setLength(0);
    cursorX = 0;
    historyIndex = -1;
    savedBuffer = "";
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

}
