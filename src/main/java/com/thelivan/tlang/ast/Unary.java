package com.thelivan.tlang.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Unary extends Expression {
  @Getter
  private final Operation operation;
  @Getter
  private final Expression expression;
}
