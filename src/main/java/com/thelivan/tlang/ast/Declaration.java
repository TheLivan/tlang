package com.thelivan.tlang.ast;

import com.thelivan.tlang.parser.lexer.token.Token;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Declaration extends Element {
  private final Token name;
}
