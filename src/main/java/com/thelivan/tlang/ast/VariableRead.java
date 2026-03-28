package com.thelivan.tlang.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VariableRead extends Expression {

  private final Declaration declaration;
}
