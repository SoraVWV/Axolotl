package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.Tokenizer;
import axl.compiler.analysis.lexical.TokenizerFrame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class IllegalSyntaxException extends RuntimeException {

    private final @NonNull String message;

    public IllegalSyntaxException(String message, TokenStream tokenStream) {
        super(message);
        this.message = formatErrorMessage(tokenStream, tokenStream.get(), message);
    }

    private static String formatErrorMessage(TokenStream tokenizer, Token token, String message) {
        final String format = """
                Lexical error in row %d and column %d. %s
                
                %s ╭╶───╴%s:%d:%d╶───╴
                %d │ %s
                %s │ %s╰ %s
                """;

        int leftWhitespaces = getDigits(token.getLine());
        String whitespaces = " ".repeat(leftWhitespaces);
        String[] contentLines = tokenizer.getFile().getContent().split("\n", token.getLine() + 1);
        String errorLine = contentLines[token.getLine() - 1];

        return format.formatted(
                token.getLine(), token.getColumn(), message,
                whitespaces, tokenizer.getFile().getFilename(), token.getLine(), token.getColumn(),
                token.getLine(), errorLine,
                whitespaces, " ".repeat(token.getColumn() - 1), message
        );
    }

    private static int getDigits(int n) {
        if (n < 10)
            return 1;

        return 1 + getDigits(n / 10);
    }
}
