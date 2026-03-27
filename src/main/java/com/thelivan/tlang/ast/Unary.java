package com.thelivan.tlang.ast;

import lombok.Getter;

public class Unary extends Expression {
  @Getter
  private final Operation operation;
  @Getter
  private final Expression expression;

  public Unary(Operation operation, Expression expression) {
    this.operation = operation;
    this.expression = expression;
  }
}
