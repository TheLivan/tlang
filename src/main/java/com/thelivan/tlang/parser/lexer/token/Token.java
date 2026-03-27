package com.thelivan.tlang.parser.lexer.token;

import lombok.Data;

@Data
public class Token {
  private final String value;
  private final TokenKind tokenKind;

  @Override
  public String toString() {
    return value + " -> " + tokenKind;
  }
}
