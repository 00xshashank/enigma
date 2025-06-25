package org.foxx.enigma.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) {
        if (args.length == 1) {
            String path = args[0];
            try {
                defineAST(
                        path,
                        "Expr",
                        List.of(
                                "Assignment: Token name, Expr expression",
                                "Binary: Expr left, Token operator, Expr right",
                                "Call: Expr callee, Token paren, List<Expr> arguments",
                                "Grouping: Expr expression",
                                "Get: Expr object, Token name",
                                "Literal: Object value",
                                "Logical: Expr left, Token operator, Expr right",
                                "Set: Expr object, Token name",
                                "Unary: Token operator, Expr right",
                                "Variable: Token name"
                        )
                );
                System.out.println("Generated AST at path: " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: GenerateAst [path]");
        }
    }

    public static void defineAST(String path, String baseName, List<String> types) throws IOException {
        String fileName = path + "/" + baseName + ".java";
        try {
            PrintWriter pw = new PrintWriter(fileName);

            pw.println("package org.foxx.enigma;");
            pw.println();
            pw.println("import java.util.List;");
            pw.println();
            pw.println("abstract class " + baseName + " {");

            defineVisitor(pw, baseName, types);

            for (String type: types) {
                defineType(pw, baseName, type);
            }

            pw.println("\tabstract <R> R accept(Visitor<R> visitor);");
            pw.println("}");
            // pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    public static void defineVisitor(PrintWriter pw, String baseName, List<String> types) {
        pw.println("\tinterface Visitor<R> {");

        for (String type: types) {
            String typeName = type.split(":")[0].trim();
            pw.print("\t\tR visit" + typeName + baseName);
            pw.println("( " + baseName + "." + typeName + " " + baseName.toLowerCase() + " );");
        }
        pw.println("\t}");
    }

    public static void defineType(PrintWriter pw, String baseName, String type) {
        String typeName = type.split(":")[0].trim();
        String attributes = type.split(":")[1].trim();
        String[] attributeList = attributes.split(",");
        pw.println("\tclass " + typeName + " {");
        pw.println("\t\t" + typeName + " (" + attributes + ") {");
        for (String attribute: attributeList) {
            String name = attribute.split(" ")[attribute.split(" ").length - 1];
            pw.println("\t\t\tthis." + name.toLowerCase() + " = " + name + ";");
        }
        pw.println("\t\t}");
        pw.println();

        for (String attribute: attributeList) {
            pw.println("\t\t\tpublic final " + attribute.trim() + ";");
        }

        pw.println("\t\tpublic <R> R accept(Visitor<R> visitor) {");
        pw.println("\t\t\treturn visitor.visit" + typeName + baseName + "(this);");
        pw.println("\t\t}");
        pw.println("\t}");
    }
}
