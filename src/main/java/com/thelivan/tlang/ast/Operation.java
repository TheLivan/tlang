package com.thelivan.tlang.ast;

import com.thelivan.tlang.parser.lexer.token.TokenKind;

public enum Operation {
  UNARY_NEGATIVE,
  UNARY_POSITIVE,
  ADDITION,
  SUBSTRACTION,
  MULTIPLICATION,
  DIVISION;

  public static Operation fromToken(TokenKind kind) {
    return switch (kind) {
      case PLUS -> UNARY_POSITIVE;
      case MINUS -> UNARY_NEGATIVE;
      default -> throw new IllegalArgumentException("not unary");
    };
  }
}
