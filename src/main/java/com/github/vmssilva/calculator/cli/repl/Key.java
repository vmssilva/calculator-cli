package com.github.vmssilva.calculator.cli.repl;

public record Key(String value, KeyType type) {
  public char raw() {
    return value.charAt(value.length() - 1);
  }

  public static Key of(String key) {

    if (key.contains("+")) {
      String[] args = key.split("\\+");
      if (args.length == 2) {
        KeyType type = Enum.valueOf(KeyType.class, args[0]);
        return new Key(key.toUpperCase(), type);
      }

      throw new UnsupportedOperationException("Invalid key: '" + key + "'");
    }

    return new Key(key.toUpperCase(), Enum.valueOf(KeyType.class, key));
  }
}
