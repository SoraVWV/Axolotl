package axl.compiler;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.state.ExpressionState;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.state.expression.Expression;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Consumer;

public class Main {

    public static IFile file;

    @SneakyThrows
    public static void main(String[] args) {
        File fileAXL = new File(args[0]);
        String filename = fileAXL.getName();
        String content = Files.readString(fileAXL.toPath());
        file = new axl.compiler.File(filename, content);

        TokenStream stream = file.createTokenStream();
        Thread.sleep(5000);

        Consumer<Expression> expressionConsumer = expression -> {

        };
        long point = System.currentTimeMillis();
        ExpressionState analyzer = new ExpressionState();



        Expression expression = analyzer.analyze();





        long time = ((int) (System.currentTimeMillis() - point));
        //System.out.println(formatString(expression));
        System.out.println((int) time + " ms");
        Thread.sleep(5000);

    }

    private static void parse() {
        TokenStream stream = file.createTokenStream();
        SyntaxAnalyzer<axl.compiler.analysis.syntax.ast.File> analyzer = new DefaultSyntaxAnalyzer(stream);
        axl.compiler.analysis.syntax.ast.@NonNull File root = analyzer.analyze();
        System.out.println(formatString(root));
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