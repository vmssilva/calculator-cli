package com.github.vmssilva.calculator.cli.repl.terminal;

import java.io.IOException;
import java.io.InputStream;

public interface Terminal {

  void setup() throws IOException, InterruptedException;

  void restore() throws IOException, InterruptedException;

  InputStream in();

}
