package com.thelivan.tlang.ast;

import com.thelivan.tlang.parser.lexer.token.Token;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Literal extends Expression {
  @Getter
  private final Token token;
}
