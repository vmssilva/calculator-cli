package com.github.vmssilva.calculator.cli;

import java.io.IOException;
import java.util.Scanner;

import com.github.vmssilva.calculator.api.ast.Expression;
import com.github.vmssilva.calculator.api.parser.RecursiveAstParser;

public class CalculatorApp {

  public static void main(String[] args) throws UnsupportedOperationException {
    if (args.length > 0) {
      printResult(String.join("", args));
    } else {
      interactive();
    }
  }

  private static void printResult(String input) throws NumberFormatException, UnsupportedOperationException {
    try {
      var resut = evaluate(input);
      System.out.println(resut);
    } catch (NumberFormatException | UnsupportedOperationException e) {
      throw new UnsupportedOperationException("Malformed expression");
    }
  }

  private static void interactive() {
    Scanner scanner = new Scanner(System.in);

    // ANSI codes para cores
    final String RESET = "\u001B[0m";
    final String CYAN = "\u001B[36m";
    final String GREEN = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    final String RED = "\u001B[31m";

    // Operations counter
    int count = 0;

    System.out.println(CYAN + "======================================" + RESET);
    System.out.println(CYAN + "  Simple Calculator CLI" + RESET);
    System.out.println(CYAN + "  Commands: " + GREEN + "clear" + RESET + ", " + RED + "q (quit)" + RESET);
    System.out.println(CYAN + "  Examples: 2+2, 3*4/2, (1+2), 5(10%3)" + RESET);
    System.out.println(CYAN + "======================================" + RESET);

    String input;

    while (true) {
      System.out.print(YELLOW + "calc> " + RESET);
      input = scanner.nextLine().trim();

      if (input.equalsIgnoreCase("q")) {
        System.out.println(GREEN + "Goodbye! You performed " + count + " operations." + RESET);
        break;
      }

      if (input.equalsIgnoreCase("clear")) {
        clear();
        continue;
      }

      if (input.isEmpty())
        continue;

      try {
        printResult(input);
        count++;
      } catch (NumberFormatException | UnsupportedOperationException e) {
        System.out.println(RED + "Malformed expression" + RESET);
      }
    }

    scanner.close();
  }

  private static void clear() {

    try {
      final String os = System.getProperty("os.name");

      if (os.contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls")
            .inheritIO()
            .start()
            .waitFor();
      } else {
        new ProcessBuilder("/usr/bin/clear")
            .inheritIO()
            .start()
            .waitFor();
      }
    } catch (InterruptedException | IOException e) {
      System.out.println("Unknown error");
    }
  }

  private static Double evaluate(String input) throws NumberFormatException, UnsupportedOperationException {
    RecursiveAstParser parser = new RecursiveAstParser();
    Expression expression = parser.parse(input);
    return expression.interpret();
  }
}
