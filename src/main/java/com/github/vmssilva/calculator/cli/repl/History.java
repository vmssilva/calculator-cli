package com.github.vmssilva.calculator.cli.repl;

import java.util.ArrayList;
import java.util.List;

public class History {
  private List<String> contents = new ArrayList<>();

  public String get(int index) {
    return (isAtBoundary(index)) ? contents.get(index) : null;
  }

  public void add(String value) {
    contents.add(value);
  }

  public int size() {
    return contents.size();
  }

  private boolean isAtBoundary(int index) {
    return index >= 0 && index < contents.size();
  }
}
