package axl.compiler;

import axl.compiler.analysis.lexical.utils.Frame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.SyntaxAnalyzerAgent;
import axl.compiler.analysis.syntax.ast.Node;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

public class Main {

    public static IFile file;

    @SneakyThrows
    public static void main(String[] args) {
        File fileAXL = new File(args[0]);
        String filename = fileAXL.getName();
        String content = Files.readString(fileAXL.toPath());
        file = new axl.compiler.File(filename, content);

        TokenStream stream = file.createTokenStream();
        SyntaxAnalyzer syntaxAnalyzer = SyntaxAnalyzerAgent.createSyntaxAnalyzer();
        Frame frame = stream.createFrame();
        long point = System.currentTimeMillis();
        Node add = syntaxAnalyzer.analyze(stream);
        long time = ((int) (System.currentTimeMillis() - point));

        System.out.println(formatString(add.toString()));

        System.out.println((int) time + " ms");

        stream.restoreFrame(frame);
        while (stream.hasNext())
            stream.next();
    }

    public static String formatString(String input) {
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