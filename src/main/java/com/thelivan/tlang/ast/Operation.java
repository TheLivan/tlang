package com.thelivan.tlang.ast;

import com.thelivan.tlang.parser.lexer.token.TokenKind;

public enum Operation {
  U_POS,
  U_NEG,
  ADD,
  SUB,
  MUL,
  DIV;

  public static Operation fromToken(TokenKind kind) {
    return switch (kind) {
      case PLUS -> U_POS;
      case MINUS -> U_NEG;
      default -> throw new IllegalArgumentException("not unary");
    };
  }
}
