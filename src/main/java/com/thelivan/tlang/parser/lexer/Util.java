package com.thelivan.tlang.parser.lexer;

public class Util {
  static boolean isDigit(char in) {
    return in >= '0' && in <= '9';
  }

  static boolean isParenOpen(char in) {
    return in == '(';
  }

  static boolean isParenClose(char in) {
    return in == ')';
  }

  static boolean isParen(char in) {
    return isParenOpen(in) || isParenClose(in);
  }

  static boolean isOperator(char in) {
    return in == '+' || in == '-' || in == '*' || in == '/';
  }

  static boolean isBlank(char in) {
    return in == ' ' || in == '\n' || in == '\t' || in == '\f' ||in == '\r' || in == '\0'; //  in == '\v' ||
  }

  static boolean isSeparator(char in) {
    return isParen(in) || isBlank(in) || isOperator(in);
  }
}
