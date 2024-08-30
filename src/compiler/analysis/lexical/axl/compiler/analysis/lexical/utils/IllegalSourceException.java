package axl.compiler.analysis.lexical.utils;

import axl.compiler.analysis.lexical.Tokenizer;
import axl.compiler.analysis.lexical.TokenizerFrame;
import lombok.Getter;
import lombok.NonNull;

public class IllegalSourceException extends RuntimeException {

    private static String format = """
            
            %s ╭╶───╴%s:%d:%d╶───╴
            %d │ %s
            %s │ %s╰ %s
            """;

    @Getter
    @NonNull
    private final String message;

    public IllegalSourceException(String message, Tokenizer tokenizer, TokenizerFrame frame) {
        super(message);
        int leftWhitespaces = digits(frame.getLine());
        String whitespaces = " ".repeat(leftWhitespaces);
        this.message = format.formatted(
                whitespaces, tokenizer.getFile().getFilename(), frame.getLine(), frame.getColumn(),
                frame.getLine(), tokenizer.getFile().getContent().split("\n", frame.getLine() + 1)[frame.getLine() - 1],
                whitespaces, " ".repeat(frame.getColumn() - 1), message
        );
    }

    private static int digits(int n) {
        if (n < 10)
            return 1;

        return 1 + digits(n / 10);
    }
}
