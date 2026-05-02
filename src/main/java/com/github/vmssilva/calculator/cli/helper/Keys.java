package com.github.vmssilva.calculator.cli.helper;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.KeyType;

public class Keys {

  public static boolean isLetter(Key key) {
    if (key.type() != KeyType.REGULAR)
      return false;

    return Character.isLetter(key.value().charAt(0));
  }

  public boolean isDigit(Key key) {
    if (key.type() != KeyType.REGULAR)
      return false;

    return Character.isDigit(key.value().charAt(0));
  }

  public boolean isSymbol(Key key) {
    return key.type() == KeyType.REGULAR && !isLetter(key) && !isDigit(key);
  }
}
