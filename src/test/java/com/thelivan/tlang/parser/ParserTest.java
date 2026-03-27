package com.thelivan.tlang.parser;

import org.junit.jupiter.api.Test;

public class ParserTest {

  @Test
  void simpleTwoLiterals() {
    Parser parser = new Parser();
    parser.of("(7 + 6) * (42 + 56)");
    parser.program();
  }

}
