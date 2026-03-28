package com.thelivan.tlang.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Binary extends Expression {
  @Getter
  private final Expression left;
  @Getter
  private final Expression right;
  @Getter
  private final Operation operation;
}
