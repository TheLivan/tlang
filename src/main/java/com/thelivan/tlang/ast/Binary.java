package com.thelivan.tlang.ast;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Binary extends Expression {
  private final Expression left;
  private final Expression right;
  private final Operation operation;
}
