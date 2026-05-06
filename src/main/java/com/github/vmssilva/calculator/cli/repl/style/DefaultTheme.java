package com.github.vmssilva.calculator.cli.repl.style;

public class DefaultTheme implements Theme {

  @Override
  public String prompt() {
    return "calc " + Symbols.ARROW_RIGHT + " ";
  }

  @Override
  public String previewArrow() {
    return Symbols.ARROW_RIGHT;
  }

  @Override
  public String cursor() {
    return "";
  }

  // ===== colors =====
  @Override
  public Color promptColor() {
    return Color.RESET;
  }

  @Override
  public Color previewColor() {
    return Color.BRIGHT_BLACK;
  }

  @Override
  public Color cursorColor() {
    return Color.INVERT;
  }

  @Override
  public Color successColor() {
    return Color.GREEN;
  }

  @Override
  public Color errorColor() {
    return Color.RED;
  }

  @Override
  public String successSymbol() {
    return Symbols.CHECK; // ✓
  }

  @Override
  public String errorSymbol() {
    return Symbols.CROSS; // ✗
  }

  @Override
  public Color infoColor() {
    return Color.CYAN;
  }

  // ===== layout =====
  @Override
  public String borderHorizontal() {
    return Symbols.H_LINE;
  }

  @Override
  public String borderVertical() {
    return Symbols.V_LINE;
  }

  @Override
  public String cornerTopLeft() {
    return Symbols.TOP_LEFT;
  }

  @Override
  public String cornerTopRight() {
    return Symbols.TOP_RIGHT;
  }

  @Override
  public String cornerBottomLeft() {
    return Symbols.BOTTOM_LEFT;
  }

  @Override
  public String cornerBottomRight() {
    return Symbols.BOTTOM_RIGHT;
  }

  @Override
  public int padding() {
    return 1;
  }
}
