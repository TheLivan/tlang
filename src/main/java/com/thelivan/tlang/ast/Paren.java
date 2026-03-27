package com.thelivan.tlang.ast;

import lombok.Getter;

public class Paren extends Expression {

  @Getter
  private final Expression expression;

  public Paren(Expression expression) {
    this.expression = expression;
  }
}
