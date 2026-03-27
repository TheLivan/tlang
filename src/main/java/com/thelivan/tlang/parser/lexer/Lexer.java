package com.thelivan.tlang.parser.lexer;

import static com.thelivan.tlang.parser.lexer.Util.isBlank;
import static com.thelivan.tlang.parser.lexer.Util.isDigit;
import static com.thelivan.tlang.parser.lexer.Util.isOperator;
import static com.thelivan.tlang.parser.lexer.Util.isSeparator;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.ASTERISK;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.ERROR;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.MINUS;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.NUMBER;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PAREN_CLOSE;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PAREN_OPEN;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.PLUS;
import static com.thelivan.tlang.parser.lexer.token.TokenKind.SLASH;

import com.thelivan.tlang.parser.lexer.token.Token;
import com.thelivan.tlang.parser.lexer.token.TokenKind;

public class Lexer {
  private String value;
  private Token previewToken;
  private int start;
  private int end;

  public void of(String input) {
    this.value = input;
    this.end = this.start = 0;
    this.previewToken = null;
    skipSpaces();
  }

  public Token next() {
    var token = peek();
    this.previewToken = null;
    return token;
  }

  public Token peek() {
    if (previewToken != null)
      return previewToken;

    if (!good()) {
      return consume(TokenKind.EOF);
    }

    var next = peekChar();
    if (isDigit(next)) {
      return number();
    } else if (isOperator(next)) {
      return op();
    } else if (isSeparator(next)) {
      return punctuation();
    }

    return consume(ERROR);
  }

  private boolean good() {
    return end != value.length();
  }

  private char peekChar() {
    return good() ? value.charAt(end) : 0;
  }

  private void advance() {
    if (good()) {
      ++end;
    }
  }

  private String readString() {
    return value.substring(start, end);
  }

  private void skipSpaces() {
    while (good() && isBlank(peekChar())) {
      advance();
    }

    this.start = end;
  }

  private Token consume(TokenKind kind) {
    if (kind == ERROR) {
      while (!isSeparator(peekChar())) {
        advance();
      }
    }

    var value = kind != TokenKind.EOF ? readString() : null;
    skipSpaces();
    this.previewToken = new Token(value, kind);
    return previewToken;
  }

  private Token punctuation() {
    var next = peekChar();
    advance();
    var result = switch (next) {
      case '(' -> PAREN_OPEN;
      case ')' -> PAREN_CLOSE;
      default -> ERROR;
    };

    return consume(result);
  }

  private Token op() {
    var next = peekChar();
    advance();
    var result = switch (next) {
      case '+' -> PLUS;
      case '-' -> MINUS;
      case '*' -> ASTERISK;
      case '/' -> SLASH;
      default -> ERROR;
    };

    return consume(result);
  }

  private Token number() {
    var result = NUMBER;
    while (good()) {
      var next = peekChar();
      if (isSeparator(next)) {
        break;
      }

      advance();

      if (isDigit(next)) {
        continue;
      }

      result = ERROR;
    }
    return consume(result);
  }
}
