package org.foxx.enigma;

import java.util.Objects;

public class Token {
    public Token(TokenType type, String name, int line) {
        this.type = type;
        if (name.isEmpty()) { this.name = ""; }
        else { this.name = name; }
        this.line = line;
    }

    public final TokenType type;
    public final int line;
    public final String name;

    @Override
    public String toString() {
        return this.type.toString();
    }
}
