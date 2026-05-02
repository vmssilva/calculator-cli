package com.github.vmssilva.calculator.cli.repl.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.command.BackspaceCommand;
import com.github.vmssilva.calculator.cli.repl.command.Command;
import com.github.vmssilva.calculator.cli.repl.command.DeleteCommand;
import com.github.vmssilva.calculator.cli.repl.command.EndCommand;
import com.github.vmssilva.calculator.cli.repl.command.HomeCommand;
import com.github.vmssilva.calculator.cli.repl.command.InsertCommand;
import com.github.vmssilva.calculator.cli.repl.command.LeftCommand;
import com.github.vmssilva.calculator.cli.repl.command.RightCommand;

public class KeyMapper {

  private final Map<Key, Command> actions;

  public KeyMapper() {
    this.actions = new HashMap<>();
    loadActions();
  }

  public void register(List<Key> keys, Command command) {
    keys.forEach((key) -> register(key, command));
  }

  public void register(Key key, Command command) {
    actions.put(key, command);
  }

  public Command map(Key key) {
    if (actions.containsKey(key))
      return actions.get(key);

    return switch (key.type()) {
      case REGULAR -> new InsertCommand(key);
      case SPACE -> new InsertCommand(new Key(" ", KeyType.REGULAR));
      case BACKSPACE -> new BackspaceCommand();
      case DELETE -> new DeleteCommand();
      case LEFT -> new LeftCommand();
      case RIGHT -> new RightCommand();
      case HOME -> new HomeCommand();
      case END -> new EndCommand();
      default -> null;
    };
  }

  private void loadActions() {

    register(Key.of("ENTER"), s -> {
      s.buffer.setLength(0);
      s.cursor = 0;
    });

    register(List.of(Key.of("CTRL+C"), Key.of("CTRL+D")), (s) -> {
      System.exit(1);
    });

    register(Key.of("CTRL+U"), (s) -> {
      s.buffer.delete(0, s.cursor);
      s.cursor = 0;
    });

    register(Key.of("CTRL+K"), (s) -> {
      s.buffer.delete(s.cursor, s.buffer.length());
    });

    register(Key.of("CTRL+L"), (s) -> s.effects.clearScreen());

    register(Key.of("UP"), (s) -> {

      if (s.history.size() <= 0)
        return;

      if (s.historyIndex == -1) {
        s.savedBuffer = s.buffer.toString();
        s.historyIndex = s.history.size() - 1;
      } else {
        if (s.historyIndex > 0) {
          s.historyIndex--;
        }
      }

      s.buffer.setLength(0);
      s.buffer.append(s.history.get(s.historyIndex));
      s.cursor = s.buffer.length();
    });

    register(Key.of("DOWN"), (s) -> {
      if (s.historyIndex == -1)
        return;

      if (s.historyIndex < s.history.size() - 1) {
        s.historyIndex++;
        s.buffer.setLength(0);
        s.buffer.append(s.history.get(s.historyIndex));
        s.cursor = s.buffer.length();
      } else {
        s.historyIndex = -1;

        s.buffer.setLength(0);
        s.buffer.append(s.savedBuffer);
        s.cursor = s.buffer.length();
      }

    });

    register(Key.of("TAB"), (s) -> {
      var value = s.buffer.toString();

      if (!(value.startsWith("!")))
        return;

      try {
        int index = Integer.valueOf(value.substring(1));

        if (index < 0 || index >= s.history.size())
          return;

        s.buffer.setLength(0);
        s.buffer.append(s.history.get(index));
        s.cursor = s.buffer.length();

      } catch (NumberFormatException e) {
        return;
      }
    });
  }
}
