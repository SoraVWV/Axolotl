package axl.compiler.analysis.lexical.utils;

import axl.compiler.analysis.lexical.Tokenizer;
import axl.compiler.analysis.lexical.TokenizerFrame;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class IllegalSourceException extends RuntimeException {

    private final @NonNull String message;

    public IllegalSourceException(String message, Tokenizer tokenizer, TokenizerFrame frame) {
        super(message);
        this.message = formatErrorMessage(tokenizer, frame, message);
    }

    private static String formatErrorMessage(Tokenizer tokenizer, TokenizerFrame frame, String message) {
        final String format = """
                Lexical error in row %d and column %d. %s
                
                %s ╭╶───╴%s:%d:%d╶───╴
                %d │ %s
                %s │ %s╰ %s
                """;

        int leftWhitespaces = getDigits(frame.line());
        String whitespaces = " ".repeat(leftWhitespaces);
        String[] contentLines = tokenizer.getFile().getContent().split("\n", frame.line() + 1);
        String errorLine = contentLines[frame.line() - 1];

        return format.formatted(
                frame.line(), frame.column(), message,
                whitespaces, tokenizer.getFile().getFilename(), frame.line(), frame.column(),
                frame.line(), errorLine,
                whitespaces, " ".repeat(frame.column() - 1), message
        );
    }

    private static int getDigits(int n) {
        if (n < 10) {
            return 1;
        }
        return 1 + getDigits(n / 10);
    }
}