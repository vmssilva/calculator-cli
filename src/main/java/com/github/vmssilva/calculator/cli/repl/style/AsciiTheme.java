package com.github.vmssilva.calculator.cli.repl.style;

public class AsciiTheme implements Theme {

  @Override
  public String prompt() {
    return "calc > ";
  }

  @Override
  public String previewArrow() {
    return ">";
  }

  @Override
  public String cursor() {
    return "_";
  }

  // ===== colors =====
  @Override
  public Color promptColor() {
    return Color.RESET;
  }

  @Override
  public Color previewColor() {
    return Color.RESET;
  }

  @Override
  public Color cursorColor() {
    return Color.RESET;
  }

  @Override
  public Color successColor() {
    return Color.RESET;
  }

  @Override
  public Color errorColor() {
    return Color.RESET;
  }

  @Override
  public String successSymbol() {
    return "OK";
  }

  @Override
  public String errorSymbol() {
    return "ERR";
  }

  @Override
  public Color infoColor() {
    return Color.RESET;
  }

  // ===== layout =====
  @Override
  public String borderHorizontal() {
    return "-";
  }

  @Override
  public String borderVertical() {
    return "|";
  }

  @Override
  public String cornerTopLeft() {
    return "+";
  }

  @Override
  public String cornerTopRight() {
    return "+";
  }

  @Override
  public String cornerBottomLeft() {
    return "+";
  }

  @Override
  public String cornerBottomRight() {
    return "+";
  }

  @Override
  public int padding() {
    return 1;
  }
}
