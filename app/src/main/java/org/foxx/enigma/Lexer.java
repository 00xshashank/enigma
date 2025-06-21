package org.foxx.enigma;

import java.util.Iterator;
import java.util.function.Consumer;

public class Lexer implements Iterator<Token> {
    public Lexer(String text) {
        this.text = text;
        this.current = 0;
        this.line = 0;
    }
    public final String text;
    public int current;
    public int line;

    public boolean isAtEnd() {
        return text.length() <= current;
    }

    public char peek() {
        return text.charAt(current);
    }
    public char advance() {
        if (isAtEnd()) {
            return '\0';
        }
        return text.charAt(current++);
    }

    public boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    public boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    public Token identifier() {
        int start = current - 1;
        while (!isAtEnd() && (isAlpha(text.charAt(current)) || isDigit(text.charAt(current)))) {
            advance();
        }

        advance();
        String name = text.substring(start, current);
        return new Token(TokenType.TOKEN_IDENTIFIER, name, line);
    }

    public Token number() {
        int start = current - 1;
        while (!isAtEnd() && isDigit(text.charAt(current))) {
            advance();
        }
        if (!isAtEnd() && peek() == '.') {
            advance();
            while (!isAtEnd() && isDigit(text.charAt(current))) {
                advance();
            }
        }

        advance();
        String value = text.substring(start, current);
        return new Token(TokenType.TOKEN_NUMBER, value, line);
    }

    public Token string() {
        int start = current;
        while(!isAtEnd() && !match('"')) {
            advance();
        }
        if (isAtEnd()) {
            // Replace with proper error classes and message
            System.out.println("Error: Unterminated string.");
        }

        advance();
        String value = text.substring(start, current);
        return new Token(TokenType.TOKEN_STRING, value, line);
    }

    public boolean match (char c) {
        if (isAtEnd()) { return false; }
        if (peek() == c) {
            advance();
            return true;
        }
        return false;
    }

    public void skipWhitespace() {
        while (true) {
            char c = peek();

            switch (c) {
                case ' ':
                case '\t':
                    advance();
                    break;

                case '\n':
                case '\r':
                    line++;
                    advance();
                    break;

                default:
                    return;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !isAtEnd();
    }

    @Override
    public Token next() {
        skipWhitespace();
        char c = advance();

        if (isAlpha(c)) { return identifier(); }
        if (isDigit(c)) { return number(); }

        TokenType type = TokenType.TOKEN_ERROR;
        String lexeme = "";
        switch (c) {
            case '(': type = TokenType.TOKEN_LEFT_PAREN; break;
            case ')': type = TokenType.TOKEN_RIGHT_PAREN; break;
            case '[': type = TokenType.TOKEN_LEFT_BRACE; break;
            case ']': type = TokenType.TOKEN_RIGHT_BRACE; break;
            case ',': type = TokenType.TOKEN_COMMA; break;
            case '.': type = TokenType.TOKEN_DOT; break;
            case '-': type = TokenType.TOKEN_MINUS; break;
            case '+': type = TokenType.TOKEN_PLUS; break;
            case ';': type = TokenType.TOKEN_SEMICOLON; break;
            case '/': type = TokenType.TOKEN_SLASH; break;
            case '*': type = TokenType.TOKEN_STAR; break;

            case '!': {
                if (match('=')) { type = TokenType.TOKEN_BANG_EQUAL; }
                else { type = TokenType.TOKEN_BANG; }
            } break;

            case '=': {
                if (match('=')) { type = TokenType.TOKEN_EQUAL_EQUAL; }
                else { type = TokenType.TOKEN_EQUAL; }
            } break;

            case '>': {
                if (match('=')) { type = TokenType.TOKEN_GREATER_EQUAL; }
                else { type = TokenType.TOKEN_GREATER; }
            } break;

            case '<': {
                if (match('=')) { type = TokenType.TOKEN_LESS_EQUAL; }
                else { type = TokenType.TOKEN_LESS; }
            } break;

            case '"': {
                return string();
            }
        }

        return new Token(type, "", line);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super Token> action) {
        throw new UnsupportedOperationException();
    }
}
