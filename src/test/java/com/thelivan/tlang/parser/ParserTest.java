package com.thelivan.tlang.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.thelivan.tlang.ast.Binary;
import com.thelivan.tlang.ast.Expression;
import com.thelivan.tlang.ast.Literal;
import com.thelivan.tlang.ast.Operation;
import com.thelivan.tlang.ast.Paren;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ParserTest {

  @Test
  @Disabled
  void simpleTwoLiterals() {
    Parser parser = new Parser();
    parser.of("(7 + 6) * (42 + 56)");
    Expression expr = parser.program();
    Binary binary = (Binary) expr;

    assertInstanceOf(Binary.class, expr);
    assertInstanceOf(Paren.class, binary.getLeft());
    assertInstanceOf(Paren.class, binary.getRight());
    assertSame(Operation.MUL, binary.getOperation());

    Expression left = ((Paren) binary.getLeft()).getExpression();
    assertInstanceOf(Binary.class, left);
    Binary left_b = (Binary) left;
    assertInstanceOf(Literal.class, left_b.getLeft());
    assertInstanceOf(Literal.class, left_b.getRight());

    Expression right = ((Paren) binary.getRight()).getExpression();
    assertInstanceOf(Binary.class, right);
    Binary right_b = (Binary) right;
    assertInstanceOf(Literal.class, right_b.getLeft());
    assertInstanceOf(Literal.class, right_b.getRight());
  }

  @Test
  void mul() {
    Parser parser = new Parser();
    parser.of("7 / 6 / 42 / 56");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;

    assertInstanceOf(Literal.class, binary.getLeft());
    assertInstanceOf(Binary.class, binary.getRight());
    assertSame(Operation.DIV, binary.getOperation());

    Binary right = (Binary) binary.getRight();
    assertSame(Operation.DIV, right.getOperation());
    assertInstanceOf(Literal.class, right.getLeft());
    assertInstanceOf(Binary.class, right.getRight());

    Binary rightRight = (Binary) right.getRight();
    assertSame(Operation.DIV, rightRight.getOperation());
    assertInstanceOf(Literal.class, rightRight.getLeft());
    assertInstanceOf(Literal.class, rightRight.getRight());
  }
}
