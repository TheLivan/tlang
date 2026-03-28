package com.thelivan.tlang.ast;

import com.thelivan.tlang.parser.lexer.token.Token;
import lombok.Getter;

@Getter
public class VariableDeclaration extends Declaration {

  private final Expression initializer;

  public VariableDeclaration(Token name, Expression initializer) {
    super(name);
    this.initializer = initializer;
  }
}
