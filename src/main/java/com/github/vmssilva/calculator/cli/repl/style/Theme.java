package com.github.vmssilva.calculator.cli.repl.style;

public interface Theme {

  // ===== symbols =====
  String prompt();

  String previewArrow();

  String cursor();

  String successSymbol();

  String errorSymbol();

  // ===== semantic colors =====
  Color promptColor();

  Color previewColor();

  Color cursorColor();

  Color successColor();

  Color errorColor();

  Color infoColor();

  // ===== layout =====
  String borderHorizontal();

  String borderVertical();

  String cornerTopLeft();

  String cornerTopRight();

  String cornerBottomLeft();

  String cornerBottomRight();

  int padding(); // padding interno (ex: 1 espaço)
}
