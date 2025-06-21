package org.foxx.enigma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Enigma {
    public static void run(String line) {
        Lexer lexer = new Lexer(line);
        while (lexer.hasNext()) {
            System.out.println(lexer.next());
        }
    }

    public static void repl() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        while (true) {
            System.out.print(">> ");
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println("Error while reading line:");
                System.out.println(e);
                return;
            }

            run(line);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            // REPL
            repl();
        } else if (args.length == 1) {
            // Run a file
        } else {
            System.err.println("Usage: enigma [script]");
        }
    }
}
