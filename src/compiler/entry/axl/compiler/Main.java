package axl.compiler;

import axl.compiler.analysis.lexical.utils.Frame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

public class Main {

    public static IFile file;

    @SneakyThrows
    public static void main(String[] args) {
//        System.setProperty("org.graphstream.ui", );
        File fileAXL = new File(args[0]);
        String filename = fileAXL.getName();
        String content = Files.readString(fileAXL.toPath());
        file = new axl.compiler.File(filename, content);

        TokenStream stream = file.createTokenStream();
        String ast = new DefaultSyntaxAnalyzer(stream).analyze().toString();
        System.out.println(formatString(ast));
    }

    public static String formatString(Object obj) {
        if (obj == null)
            return "null";

        String input = obj.toString();
        input = input.replace(" ", "");
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;

        for (char c : input.toCharArray()) {
            if (c == '{' || c == '[') {
                if (c == '{')
                    formatted.append(" ");

                formatted.append(c).append("\n");
                formatted.append(" ".repeat(++indentLevel * 4));
            } else if (c == '}' || c == ']') {
                formatted.append("\n").append(" ".repeat(--indentLevel * 4));
                formatted.append(c);
            } else if (c == ',') {
                formatted.append(c).append('\n').append(" ".repeat(indentLevel * 4));
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

}