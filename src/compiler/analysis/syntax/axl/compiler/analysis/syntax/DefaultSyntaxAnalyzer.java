package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer<File> {

    private final Stack<Expression> stack = new Stack<>();

    @Override
    public @NonNull File analyze(TokenStream stream) {
        File file = new File();
        file.setFile(stream.getFile());

        while (stream.hasNext() && eatIfTypeEquals(stream, TokenType.IMPORT))
            file.getImports().add(analyseImport(stream));

        return file;
    }

    @Contract("!null -> new; null -> fail")
    private File.@NotNull Import analyseImport(TokenStream stream) {
        List<Token> tokens = analyseByDelimiter(
                stream,
                new DelimiterTokenAnalyzer(stream, TokenType.IDENTIFY),
                TokenType.DOT
        );

        if (tokens.isEmpty())
            throw new IllegalSyntaxException("Import content cannot be empty", stream);

        if (!stream.hasNext() || !eatIfTypeEquals(stream, TokenType.DOT))
            return new File.Import(tokens, false);

        if (!stream.hasNext())
            throw new IllegalSyntaxException("The file ended unexpectedly", stream);

        if (!eatIfTypeEquals(stream, TokenType.MULTIPLY))
            throw new IllegalSyntaxException(
                    "Unexpected token'" + stream.get().getContent(stream.getFile()) + "'. Expected ID or star",
                    stream
            );

        return new File.Import(tokens, true);
    }

    public boolean eatIfTypeEquals(TokenStream stream, TokenType type) {
        if (!stream.hasNext())
            return false;

        if (stream.get().getType() == type) {
            stream.next();
            return true;
        }

        return false;
    }

    @Nullable
    public Token nextIfTypeEquals(TokenStream stream, TokenType type) {
        if (!stream.hasNext())
            return null;

        if (stream.get().getType() == type)
            return stream.next();

        return null;
    }

    private <T> List<T> analyseByDelimiter(TokenStream stream, Supplier<T> necessarySuppler, TokenType delimiter) {
        List<T> necessaryObjects = new ArrayList<>();

        for (T necessaryObject; (necessaryObject = necessarySuppler.get()) != null; ) {
            if (!stream.hasNext() || !eatIfTypeEquals(stream, delimiter))
                break;

            necessaryObjects.add(necessaryObject);
        }

        return necessaryObjects;
    }

    @AllArgsConstructor
    private class DelimiterTokenAnalyzer implements Supplier<Token> {

        private final TokenStream stream;

        private final TokenType necessary;

        @Override
        public Token get() {
            if (!stream.hasNext())
                return null;

            return DefaultSyntaxAnalyzer.this.nextIfTypeEquals(stream, necessary);
        }

    }

}
