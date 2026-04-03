package com.thelivan.tlang.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.thelivan.tlang.ast.Binary;
import com.thelivan.tlang.ast.Expression;
import com.thelivan.tlang.ast.Literal;
import com.thelivan.tlang.ast.Operation;
import com.thelivan.tlang.ast.Paren;
import com.thelivan.tlang.ast.Unary;
import org.junit.jupiter.api.Test;

public class ParserTest {

  @Test
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

    assertInstanceOf(Binary.class, binary.getLeft());
    assertInstanceOf(Literal.class, binary.getRight());
    assertSame(Operation.DIV, binary.getOperation());

    Binary left = (Binary) binary.getLeft();
    assertSame(Operation.DIV, left.getOperation());
    assertInstanceOf(Binary.class, left.getLeft());
    assertInstanceOf(Literal.class, left.getRight());

    Binary leftLeft = (Binary) left.getLeft();
    assertSame(Operation.DIV, leftLeft.getOperation());
    assertInstanceOf(Literal.class, leftLeft.getLeft());
    assertInstanceOf(Literal.class, leftLeft.getRight());
  }

  @Test
  void additionChainLeftAssociative() {
    Parser parser = new Parser();
    parser.of("1 + 2 + 3 + 4");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;

    assertInstanceOf(Binary.class, binary.getLeft());
    assertInstanceOf(Literal.class, binary.getRight());
    assertSame(Operation.ADD, binary.getOperation());

    Binary left = (Binary) binary.getLeft();
    assertSame(Operation.ADD, left.getOperation());
    assertInstanceOf(Binary.class, left.getLeft());
    assertInstanceOf(Literal.class, left.getRight());

    Binary leftLeft = (Binary) left.getLeft();
    assertSame(Operation.ADD, leftLeft.getOperation());
    assertInstanceOf(Literal.class, leftLeft.getLeft());
    assertInstanceOf(Literal.class, leftLeft.getRight());
  }

  @Test
  void multiplicationChainLeftAssociative() {
    Parser parser = new Parser();
    parser.of("2 * 3 * 4");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;

    assertInstanceOf(Binary.class, binary.getLeft());
    assertInstanceOf(Literal.class, binary.getRight());
    assertSame(Operation.MUL, binary.getOperation());

    Binary left = (Binary) binary.getLeft();
    assertSame(Operation.MUL, left.getOperation());
    assertInstanceOf(Literal.class, left.getLeft());
    assertInstanceOf(Literal.class, left.getRight());
  }

  @Test
  void mixedPrecedence() {
    Parser parser = new Parser();
    parser.of("2 + 3 * 4 - 6 / 2");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary outer = (Binary) expr;
    assertSame(Operation.SUB, outer.getOperation());

    assertInstanceOf(Binary.class, outer.getLeft());
    assertInstanceOf(Binary.class, outer.getRight());

    Binary leftAdd = (Binary) outer.getLeft();
    assertSame(Operation.ADD, leftAdd.getOperation());
    assertInstanceOf(Literal.class, leftAdd.getLeft());
    assertInstanceOf(Binary.class, leftAdd.getRight());

    Binary mul = (Binary) leftAdd.getRight();
    assertSame(Operation.MUL, mul.getOperation());
    assertInstanceOf(Literal.class, mul.getLeft());
    assertInstanceOf(Literal.class, mul.getRight());

    Binary div = (Binary) outer.getRight();
    assertSame(Operation.DIV, div.getOperation());
    assertInstanceOf(Literal.class, div.getLeft());
    assertInstanceOf(Literal.class, div.getRight());
  }

  @Test
  void unaryMinusAndPlus() {
    Parser parser = new Parser();
    parser.of("-5 + +3 * -2");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;
    assertSame(Operation.ADD, binary.getOperation());

    assertInstanceOf(Unary.class, binary.getLeft());
    Unary leftUnary = (Unary) binary.getLeft();
    assertSame(Operation.U_NEG, leftUnary.getOperation());
    assertInstanceOf(Literal.class, leftUnary.getExpression());

    assertInstanceOf(Binary.class, binary.getRight());
    Binary mul = (Binary) binary.getRight();
    assertSame(Operation.MUL, mul.getOperation());

    assertInstanceOf(Unary.class, mul.getLeft());
    Unary rightUnary = (Unary) mul.getLeft();
    assertSame(Operation.U_POS, rightUnary.getOperation());
    assertInstanceOf(Literal.class, rightUnary.getExpression());

    assertInstanceOf(Unary.class, mul.getRight());
    Unary rightmostUnary = (Unary) mul.getRight();
    assertSame(Operation.U_NEG, rightmostUnary.getOperation());
    assertInstanceOf(Literal.class, rightmostUnary.getExpression());
  }

  @Test
  void nestedParens() {
    Parser parser = new Parser();
    parser.of("((2 + 3) * (4 - 1)) / (5 + 5)");
    Expression expr = parser.program();

    assertInstanceOf(Binary.class, expr);
    Binary topDiv = (Binary) expr;
    assertSame(Operation.DIV, topDiv.getOperation());

    assertInstanceOf(Paren.class, topDiv.getLeft());
    assertInstanceOf(Paren.class, topDiv.getRight());

    Expression leftInner = ((Paren) topDiv.getLeft()).getExpression();
    assertInstanceOf(Binary.class, leftInner);
    Binary leftMul = (Binary) leftInner;
    assertSame(Operation.MUL, leftMul.getOperation());

    assertInstanceOf(Paren.class, leftMul.getLeft());
    assertInstanceOf(Paren.class, leftMul.getRight());

    Expression leftAdd = ((Paren) leftMul.getLeft()).getExpression();
    assertInstanceOf(Binary.class, leftAdd);
    Binary add = (Binary) leftAdd;
    assertSame(Operation.ADD, add.getOperation());

    Expression rightSub = ((Paren) leftMul.getRight()).getExpression();
    assertInstanceOf(Binary.class, rightSub);
    Binary sub = (Binary) rightSub;
    assertSame(Operation.SUB, sub.getOperation());

    Expression rightParen = ((Paren) topDiv.getRight()).getExpression();
    assertInstanceOf(Binary.class, rightParen);
    Binary rightAdd = (Binary) rightParen;
    assertSame(Operation.ADD, rightAdd.getOperation());
  }

  @Test
  void singleNumber() {
    Parser parser = new Parser();
    parser.of("42");
    Expression expr = parser.program();
    assertInstanceOf(Literal.class, expr);
    assertEquals("42", ((Literal) expr).getToken().value());
  }

  @Test
  void unaryOnly() {
    Parser parser = new Parser();
    parser.of("--7");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary outer = (Unary) expr;
    assertSame(Operation.U_NEG, outer.getOperation());
    assertInstanceOf(Unary.class, outer.getExpression());
    Unary inner = (Unary) outer.getExpression();
    assertSame(Operation.U_NEG, inner.getOperation());
    assertInstanceOf(Literal.class, inner.getExpression());
  }

  @Test
  void unaryMinusBeforeNumber() {
    Parser parser = new Parser();
    parser.of("-42");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary unary = (Unary) expr;
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Literal.class, unary.getExpression());
  }

  @Test
  void unaryPlusBeforeNumber() {
    Parser parser = new Parser();
    parser.of("+100");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary unary = (Unary) expr;
    assertSame(Operation.U_POS, unary.getOperation());
    assertInstanceOf(Literal.class, unary.getExpression());
  }

  @Test
  void unaryInBinaryExpression() {
    Parser parser = new Parser();
    parser.of("5 + -3");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;
    assertSame(Operation.ADD, binary.getOperation());
    assertInstanceOf(Literal.class, binary.getLeft());
    assertInstanceOf(Unary.class, binary.getRight());
    Unary unary = (Unary) binary.getRight();
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Literal.class, unary.getExpression());
  }

  @Test
  void parensWithUnary() {
    Parser parser = new Parser();
    parser.of("-(7 + 2)");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary unary = (Unary) expr;
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Paren.class, unary.getExpression());
    Paren paren = (Paren) unary.getExpression();
    assertInstanceOf(Binary.class, paren.getExpression());
  }

  @Test
  void complexWithUnaryAndPrecedence() {
    Parser parser = new Parser();
    parser.of("-2 * 3 + -4");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary add = (Binary) expr;
    assertSame(Operation.ADD, add.getOperation());

    assertInstanceOf(Binary.class, add.getLeft());
    Binary mul = (Binary) add.getLeft();
    assertSame(Operation.MUL, mul.getOperation());

    assertInstanceOf(Unary.class, mul.getLeft());
    assertSame(Operation.U_NEG, ((Unary) mul.getLeft()).getOperation());
    assertInstanceOf(Literal.class, ((Unary) mul.getLeft()).getExpression());

    assertInstanceOf(Literal.class, mul.getRight());

    assertInstanceOf(Unary.class, add.getRight());
    assertSame(Operation.U_NEG, ((Unary) add.getRight()).getOperation());
    assertInstanceOf(Literal.class, ((Unary) add.getRight()).getExpression());
  }

  @Test
  void subtractionChainLeftAssociative() {
    Parser parser = new Parser();
    parser.of("10 - 3 - 2");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;
    assertInstanceOf(Binary.class, binary.getLeft());
    assertInstanceOf(Literal.class, binary.getRight());
    assertSame(Operation.SUB, binary.getOperation());
    Binary left = (Binary) binary.getLeft();
    assertSame(Operation.SUB, left.getOperation());
    assertInstanceOf(Literal.class, left.getLeft());
    assertInstanceOf(Literal.class, left.getRight());
  }

  @Test
  void divisionAndMultiplicationSamePrecedence() {
    Parser parser = new Parser();
    parser.of("8 * 4 / 2");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;
    assertInstanceOf(Binary.class, binary.getLeft());
    assertInstanceOf(Literal.class, binary.getRight());
    Binary left = (Binary) binary.getLeft();
    assertSame(Operation.MUL, left.getOperation());
    assertInstanceOf(Literal.class, left.getLeft());
    assertInstanceOf(Literal.class, left.getRight());
    assertSame(Operation.DIV, binary.getOperation());
  }

  @Test
  void deeplyNestedParens() {
    Parser parser = new Parser();
    parser.of("(((1)))");
    Expression expr = parser.program();
    assertInstanceOf(Paren.class, expr);
    Paren p1 = (Paren) expr;
    assertInstanceOf(Paren.class, p1.getExpression());
    Paren p2 = (Paren) p1.getExpression();
    assertInstanceOf(Paren.class, p2.getExpression());
    Paren p3 = (Paren) p2.getExpression();
    assertInstanceOf(Literal.class, p3.getExpression());
  }

  @Test
  void multipleUnaryOperators() {
    Parser parser = new Parser();
    parser.of("+-+5");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary u1 = (Unary) expr;
    assertSame(Operation.U_POS, u1.getOperation());
    assertInstanceOf(Unary.class, u1.getExpression());
    Unary u2 = (Unary) u1.getExpression();
    assertSame(Operation.U_NEG, u2.getOperation());
    assertInstanceOf(Unary.class, u2.getExpression());
    Unary u3 = (Unary) u2.getExpression();
    assertSame(Operation.U_POS, u3.getOperation());
    assertInstanceOf(Literal.class, u3.getExpression());
  }

  @Test
  void largeExpression() {
    Parser parser = new Parser();
    parser.of("1 + 2 - 3 * 4 / 2 + 5 - 6");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary top = (Binary) expr;
    assertSame(Operation.SUB, top.getOperation());
    assertInstanceOf(Binary.class, top.getLeft());
    assertInstanceOf(Literal.class, top.getRight());
    Binary addLeft = (Binary) top.getLeft();
    assertSame(Operation.ADD, addLeft.getOperation());
    assertInstanceOf(Binary.class, addLeft.getLeft());
    assertInstanceOf(Binary.class, addLeft.getRight());
  }

  @Test
  void expressionWithAllOperators() {
    Parser parser = new Parser();
    parser.of("2 + 3 * 4 - 6 / 2 + 7");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary addOuter = (Binary) expr;
    assertSame(Operation.ADD, addOuter.getOperation());
    assertInstanceOf(Binary.class, addOuter.getLeft());
    assertInstanceOf(Literal.class, addOuter.getRight());
    Binary sub = (Binary) addOuter.getLeft();
    assertSame(Operation.SUB, sub.getOperation());
    assertInstanceOf(Binary.class, sub.getLeft());
    assertInstanceOf(Binary.class, sub.getRight());
  }

  @Test
  void unaryWithParensAndPrecedence() {
    Parser parser = new Parser();
    parser.of("- ( 2 + 3 ) * 4");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary mul = (Binary) expr;
    assertSame(Operation.MUL, mul.getOperation());
    assertInstanceOf(Unary.class, mul.getLeft());
    Unary unary = (Unary) mul.getLeft();
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Paren.class, unary.getExpression());
    Paren paren = (Paren) unary.getExpression();
    assertInstanceOf(Binary.class, paren.getExpression());
    assertInstanceOf(Literal.class, mul.getRight());
  }

  @Test
  void singleParenExpression() {
    Parser parser = new Parser();
    parser.of("(42)");
    Expression expr = parser.program();
    assertInstanceOf(Paren.class, expr);
    Paren paren = (Paren) expr;
    assertInstanceOf(Literal.class, paren.getExpression());
  }

  @Test
  void multipleUnaryAndBinaryMixed() {
    Parser parser = new Parser();
    parser.of("--5 + +-3");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary add = (Binary) expr;
    assertSame(Operation.ADD, add.getOperation());
    assertInstanceOf(Unary.class, add.getLeft());
    Unary leftU = (Unary) add.getLeft();
    assertSame(Operation.U_NEG, leftU.getOperation());
    assertInstanceOf(Unary.class, leftU.getExpression());
    Unary leftInner = (Unary) leftU.getExpression();
    assertSame(Operation.U_NEG, leftInner.getOperation());
    assertInstanceOf(Literal.class, leftInner.getExpression());
    assertInstanceOf(Unary.class, add.getRight());
    Unary rightU = (Unary) add.getRight();
    assertSame(Operation.U_POS, rightU.getOperation());
    assertInstanceOf(Unary.class, rightU.getExpression());
    Unary rightInner = (Unary) rightU.getExpression();
    assertSame(Operation.U_NEG, rightInner.getOperation());
    assertInstanceOf(Literal.class, rightInner.getExpression());
  }

  @Test
  void unaryOnParenResult() {
    Parser parser = new Parser();
    parser.of("-(2+3)");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary unary = (Unary) expr;
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Paren.class, unary.getExpression());
    Paren paren = (Paren) unary.getExpression();
    assertInstanceOf(Binary.class, paren.getExpression());
  }

  @Test
  void consecutiveUnaryBeforeParen() {
    Parser parser = new Parser();
    parser.of("--(5+2)");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary outer = (Unary) expr;
    assertSame(Operation.U_NEG, outer.getOperation());
    assertInstanceOf(Unary.class, outer.getExpression());
    Unary inner = (Unary) outer.getExpression();
    assertSame(Operation.U_NEG, inner.getOperation());
    assertInstanceOf(Paren.class, inner.getExpression());
  }

  @Test
  void unaryAfterBinaryNoWhitespace() {
    Parser parser = new Parser();
    parser.of("3+-2");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary binary = (Binary) expr;
    assertSame(Operation.ADD, binary.getOperation());
    assertInstanceOf(Literal.class, binary.getLeft());
    assertInstanceOf(Unary.class, binary.getRight());
    Unary unary = (Unary) binary.getRight();
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Literal.class, unary.getExpression());
  }

  @Test
  void tripleUnaryOnNumber() {
    Parser parser = new Parser();
    parser.of("+-+7");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary u1 = (Unary) expr;
    assertSame(Operation.U_POS, u1.getOperation());
    assertInstanceOf(Unary.class, u1.getExpression());
    Unary u2 = (Unary) u1.getExpression();
    assertSame(Operation.U_NEG, u2.getOperation());
    assertInstanceOf(Unary.class, u2.getExpression());
    Unary u3 = (Unary) u2.getExpression();
    assertSame(Operation.U_POS, u3.getOperation());
    assertInstanceOf(Literal.class, u3.getExpression());
  }

  @Test
  void expressionStartingWithUnaryAndParen() {
    Parser parser = new Parser();
    parser.of("-(1+2)*3");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary mul = (Binary) expr;
    assertSame(Operation.MUL, mul.getOperation());
    assertInstanceOf(Unary.class, mul.getLeft());
    Unary unary = (Unary) mul.getLeft();
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Paren.class, unary.getExpression());
    assertInstanceOf(Literal.class, mul.getRight());
  }

  @Test
  void complexUnaryInsideBinaryRightSide() {
    Parser parser = new Parser();
    parser.of("10 / -2 + -3 * -4");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary add = (Binary) expr;
    assertSame(Operation.ADD, add.getOperation());

    assertInstanceOf(Binary.class, add.getLeft());
    Binary div = (Binary) add.getLeft();
    assertSame(Operation.DIV, div.getOperation());
    assertInstanceOf(Literal.class, div.getLeft());
    assertInstanceOf(Unary.class, div.getRight());

    assertInstanceOf(Binary.class, add.getRight());
    Binary mul = (Binary) add.getRight();
    assertSame(Operation.MUL, mul.getOperation());
    assertInstanceOf(Unary.class, mul.getLeft());
    assertInstanceOf(Unary.class, mul.getRight());
  }

  @Test
  void deeplyNestedLeftAssociativeChain() {
    Parser parser = new Parser();
    parser.of("1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9 - 10");
    Expression expr = parser.program();
    for (int i = 0; i < 9; i++) {
      assertInstanceOf(Binary.class, expr);
      Binary b = (Binary) expr;
      assertSame(Operation.SUB, b.getOperation());
      assertInstanceOf(Literal.class, b.getRight());
      expr = b.getLeft();
    }
    assertInstanceOf(Literal.class, expr);
  }

  @Test
  void zeroAndNegativeNumbers() {
    Parser parser = new Parser();
    parser.of("0 + -0");
    Expression expr = parser.program();
    assertInstanceOf(Binary.class, expr);
    Binary add = (Binary) expr;
    assertSame(Operation.ADD, add.getOperation());
    assertInstanceOf(Literal.class, add.getLeft());
    assertInstanceOf(Unary.class, add.getRight());
    Unary unary = (Unary) add.getRight();
    assertSame(Operation.U_NEG, unary.getOperation());
    assertInstanceOf(Literal.class, unary.getExpression());
  }

  @Test
  void multipleUnaryBeforeNumberWithSpaces() {
    Parser parser = new Parser();
    parser.of("- - - 5");
    Expression expr = parser.program();
    assertInstanceOf(Unary.class, expr);
    Unary u1 = (Unary) expr;
    assertSame(Operation.U_NEG, u1.getOperation());
    assertInstanceOf(Unary.class, u1.getExpression());
    Unary u2 = (Unary) u1.getExpression();
    assertSame(Operation.U_NEG, u2.getOperation());
    assertInstanceOf(Unary.class, u2.getExpression());
    Unary u3 = (Unary) u2.getExpression();
    assertSame(Operation.U_NEG, u3.getOperation());
    assertInstanceOf(Literal.class, u3.getExpression());
  }

  @Test
  void parenWithMultipleUnaryInside() {
    Parser parser = new Parser();
    parser.of("(-+5)");
    Expression expr = parser.program();
    assertInstanceOf(Paren.class, expr);
    Paren paren = (Paren) expr;
    assertInstanceOf(Unary.class, paren.getExpression());
    Unary u1 = (Unary) paren.getExpression();
    assertSame(Operation.U_NEG, u1.getOperation());
    assertInstanceOf(Unary.class, u1.getExpression());
    Unary u2 = (Unary) u1.getExpression();
    assertSame(Operation.U_POS, u2.getOperation());
    assertInstanceOf(Literal.class, u2.getExpression());
  }
}
