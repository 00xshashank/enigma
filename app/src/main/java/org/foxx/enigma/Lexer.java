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

    public char previous() {
        return text.charAt(current - 1);
    }

    public boolean checkKeyword(String remaining, String next, int rLength) {
        return (remaining.length() == next.length() && remaining.equals(next));
    }

    public Token identifier() {
        int start = current - 1;

        char c = previous();
        int  rLength = 0;
        String remaining = "";
        TokenType type = TokenType.IDENTIFIER;
        switch (c) {
            case 'a': rLength = 2; remaining = "nd"; type = TokenType.AND;break;
            case 'c': rLength = 4; remaining = "lass"; type = TokenType.CLASS; break;
            case 'e': rLength = 3; remaining = "lse"; type = TokenType.ELSE; break;
            case 'f': {
                char next = advance();
                switch (next) {
                    case 'a': rLength = 3; remaining = "lse"; type = TokenType.ELSE; break;
                    case 'o': rLength = 1; remaining = "r"; type = TokenType.FOR; break;
                    case 'u': rLength = 1; remaining = "n"; type = TokenType.FUN; break;
                    default: break;
                }
            } break;
            case 'i': rLength = 1; remaining = "f"; type = TokenType.IF; break;
            case 'n': rLength = 2; remaining = "il"; type = TokenType.NIL; break;
            case 'o': rLength = 1; remaining = "r"; type = TokenType.OR; break;
            case 'p': rLength = 4; remaining = "rint"; type = TokenType.PRINT; break;
            case 'r': rLength = 5; remaining = "eturn"; type = TokenType.RETURN; break;
            case 't': {
                char next = advance();
                switch (next) {
                    case 'h': rLength = 2; remaining = "is"; type = TokenType.THIS; break;
                    case 'r': rLength = 2; remaining = "ue"; type = TokenType.TRUE; break;
                    default: break;
                }
            } break;
            case 's': rLength = 4; remaining = "uper"; type = TokenType.SUPER; break;
            case 'v': rLength = 2; remaining = "ar"; type = TokenType.VAR; break;
            case 'w': rLength = 4; remaining = "hile"; type = TokenType.WHILE; break;
        }

        if (!remaining.isEmpty()  && !isAtEnd()) {
            String next = text.substring(current, current + rLength);
            if (checkKeyword(remaining, next, rLength)) {
                for (int i=0; i<rLength; i++) {
                    advance();
                }
                return new Token(type, "", line);
            }
        }

        while (!isAtEnd() && (isAlpha(text.charAt(current)) || isDigit(text.charAt(current)))) {
            advance();
        }

        String name = text.substring(start, current);
        return new Token(TokenType.IDENTIFIER, name, line);
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
        return new Token(TokenType.NUMBER, value, line);
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
        return new Token(TokenType.STRING, value, line);
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

        TokenType type = TokenType.ERROR;
        String lexeme = "";
        switch (c) {
            case '(': type = TokenType.LEFT_PAREN; break;
            case ')': type = TokenType.RIGHT_PAREN; break;
            case '{': type = TokenType.LEFT_BRACE; break;
            case '}': type = TokenType.RIGHT_BRACE; break;
            case ',': type = TokenType.COMMA; break;
            case '.': type = TokenType.DOT; break;
            case '-': type = TokenType.MINUS; break;
            case '+': type = TokenType.PLUS; break;
            case ';': type = TokenType.SEMICOLON; break;
            case '/': type = TokenType.SLASH; break;
            case '*': type = TokenType.STAR; break;

            case '!': {
                if (match('=')) { type = TokenType.BANG_EQUAL; }
                else { type = TokenType.BANG; }
            } break;

            case '=': {
                if (match('=')) { type = TokenType.EQUAL_EQUAL; }
                else { type = TokenType.EQUAL; }
            } break;

            case '>': {
                if (match('=')) { type = TokenType.GREATER_EQUAL; }
                else { type = TokenType.GREATER; }
            } break;

            case '<': {
                if (match('=')) { type = TokenType.LESS_EQUAL; }
                else { type = TokenType.LESS; }
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
