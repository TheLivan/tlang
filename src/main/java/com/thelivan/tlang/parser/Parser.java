package com.thelivan.tlang.parser;

import static com.thelivan.tlang.parser.lexer.token.TokenKind.ASTERISK;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.MINUS;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.NUMBER;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PAREN_CLOSE;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PAREN_OPEN;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PLUS;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.SLASH;

import com.thelivan.tlang.ast.Binary;
import com.thelivan.tlang.ast.Expression;
import com.thelivan.tlang.ast.Literal;
import com.thelivan.tlang.ast.Operation;
import com.thelivan.tlang.ast.Paren;
import com.thelivan.tlang.ast.Unary;
import com.thelivan.tlang.parser.lexer.Lexer;

public class Parser {
  private Lexer lexer;

  public void of(String input) {
    this.lexer = new Lexer();
    this.lexer.of(input);
  }

  Expression program() {
    return expression();
  }

  Expression expression() {
    return addExpression();
  }

  Expression addExpression() {
    Expression left = mulExpression();
    while (true) {
      var next = lexer.peek();
      var kind = next.tokenKind();
      if (kind != PLUS && kind != MINUS) {
        break;
      }

      lexer.next();
      left = new Binary(left, mulExpression(), Operation.fromToken(kind));
    }
    return left;
  }

  Expression mulExpression() {
    Expression left = unary();
    while (true) {
      var next = lexer.peek();
      var kind = next.tokenKind();
      if (kind != ASTERISK && kind != SLASH) {
        break;
      }

      lexer.next();
      left = new Binary(left, unary(), Operation.fromToken(kind));
    }
    return left;
  }

  Expression unary() {
    var next = lexer.peek();
    var kind = next.tokenKind();
    if (kind != PLUS && kind != MINUS) {
      return primary();
    }

    lexer.next();
    return new Unary(Operation.fromTokenUnary(next.tokenKind()), primary());
  }

  Expression primary() {
    var next = lexer.peek();
    var kind = next.tokenKind();

    if (kind == PAREN_OPEN) {
      return paren();
    } else if (kind == NUMBER) {
      return literal();
    } else {
      System.out.println("error in primary-expr " + next.value());
    }
    return null;
  }

  Expression paren() {
    lexer.next();
    Expression exp = expression();
    var closeParen = lexer.next();
    if (closeParen.tokenKind() != PAREN_CLOSE) {
      System.out.println("error in paren-expr: " + closeParen.value());
    } else {
      return new Paren(exp);
    }
    return null;
  }

  Literal literal() {
    return new Literal(lexer.next());
  }
}
