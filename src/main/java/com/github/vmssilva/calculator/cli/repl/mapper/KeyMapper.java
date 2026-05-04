package com.github.vmssilva.calculator.cli.repl.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.vmssilva.calculator.cli.repl.Key;
import com.github.vmssilva.calculator.cli.repl.KeyType;
import com.github.vmssilva.calculator.cli.repl.command.BackspaceCommand;
import com.github.vmssilva.calculator.cli.repl.command.ClearLeftCommand;
import com.github.vmssilva.calculator.cli.repl.command.ClearRightCommand;
import com.github.vmssilva.calculator.cli.repl.command.ClearScreenCommand;
import com.github.vmssilva.calculator.cli.repl.command.Command;
import com.github.vmssilva.calculator.cli.repl.command.DeleteCommand;
import com.github.vmssilva.calculator.cli.repl.command.DownCommand;
import com.github.vmssilva.calculator.cli.repl.command.EndCommand;
import com.github.vmssilva.calculator.cli.repl.command.EnterCommand;
import com.github.vmssilva.calculator.cli.repl.command.HomeCommand;
import com.github.vmssilva.calculator.cli.repl.command.InsertCommand;
import com.github.vmssilva.calculator.cli.repl.command.LeftCommand;
import com.github.vmssilva.calculator.cli.repl.command.RightCommand;
import com.github.vmssilva.calculator.cli.repl.command.TabCommand;
import com.github.vmssilva.calculator.cli.repl.command.UpCommand;

public class KeyMapper {

  private final Map<Key, Command> actions = new HashMap<>();

  public void register(Key key, Command command) {
    actions.put(key, command);
  }

  public void register(List<String> keys, Command command) {
    keys.forEach(k -> register(Key.of(k), command));
  }

  public Command map(Key key) {

    Command custom = actions.get(key);
    if (custom != null)
      return custom;

    return switch (key.type()) {
      case REGULAR -> new InsertCommand(key);
      case SPACE -> new InsertCommand(new Key(" ", KeyType.REGULAR));
      case BACKSPACE -> new BackspaceCommand();
      case DELETE -> new DeleteCommand();
      case UP -> new UpCommand();
      case DOWN -> new DownCommand();
      case LEFT -> new LeftCommand();
      case RIGHT -> new RightCommand();
      case HOME -> new HomeCommand();
      case END -> new EndCommand();
      case TAB -> new TabCommand();
      case ENTER -> new EnterCommand();
      case CTRL -> handleCtrl(key);
      default -> null;
    };
  }

  private Command handleCtrl(Key key) {

    String value = key.value().substring(5).trim();

    if (!(value.isEmpty())) {

      if (value.equals("L"))
        return new ClearScreenCommand();

      if (value.equals("U"))
        return new ClearLeftCommand();

      if (value.equals("K"))
        return new ClearRightCommand();

    }

    return null;
  }
}
