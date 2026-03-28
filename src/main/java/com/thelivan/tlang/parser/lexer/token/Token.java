package com.thelivan.tlang.parser.lexer.token;

public record Token(String value, TokenKind tokenKind) {

  @Override
  public String toString() {
    return value + " = " + tokenKind;
  }
}
