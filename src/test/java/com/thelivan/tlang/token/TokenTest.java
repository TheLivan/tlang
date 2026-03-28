package com.thelivan.tlang.token;

import com.thelivan.tlang.parser.lexer.Lexer;
import com.thelivan.tlang.parser.lexer.token.TokenKind;
import org.junit.jupiter.api.Test;

class TokenTest {

  @Test
  void simple() {
    Lexer lexer = new Lexer();
    testLexer(lexer, "42 + 10");
  }

  void testLexer(Lexer lexer, String input) {
    System.out.println("input: " + input);
    lexer.of(input);
    while (true) {
      var token = lexer.next();
      System.out.println(token);
      if (token.tokenKind() == TokenKind.EOF) {
        break;
      }
    }
  }
}
