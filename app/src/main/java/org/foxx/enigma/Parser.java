package org.foxx.enigma;

public class Parser {
    public Parser(String text) {
        this.lexer = new Lexer(text);
    }

    public final Lexer lexer;

    public void parse() {}
}
