package axl.compiler.analysis.lexical.utils;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.Tokenizer;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultTokenStream implements TokenStream {

    @Getter
    @NonNull
    private final IFile file;

    private final List<Token> tokens;

    private final Tokenizer tokenizer;

    private int iterator;

    private boolean processed;

    public DefaultTokenStream(@NonNull IFile file, @NonNull List<Token> tokens, Tokenizer tokenizer) {
        this.file = file;
        this.tokens = tokens;
        this.tokenizer = tokenizer;
    }

    private DefaultTokenStream(@NonNull IFile file, @NonNull List<Token> tokens, boolean processed) {
        this.file = file;
        this.tokens = tokens;
        this.processed = processed;
        this.tokenizer = null;
    }

    @Override
    @Nullable
    public Token next() {
        if (iterator < tokens.size())
            return tokens.get(iterator++);

        if (processed)
            return null;

        tokenize();
        return next();
    }

    @Nullable
    public TokenType nextTokenType() {
        Token next = next();
        if (next == null)
            return null;

        return next.getType();
    }

    @Override
    public boolean nextTokenTypeIs(@NonNull TokenType compare) {
        return nextTokenType() == compare;
    }

    @Override
    public boolean nextTokenTypeNot(@NonNull TokenType compare) {
        return nextTokenType() != compare;
    }

    @Nullable
    @Override
    public Token get() {
        if (iterator < tokens.size())
            return tokens.get(iterator);

        if (processed)
            return null;

        tokenize();
        return get();
    }

    @Override
    public boolean hasNext() {
        return !this.processed || this.iterator < tokens.size();
    }

    @NonNull
    @Override
    public Frame createFrame() {
        return new DefaultFrame(iterator);
    }

    @Override
    public void restoreFrame(@NonNull Frame frame) {
        this.iterator = frame.getTokenId();
    }

    @NonNull
    @Override
    public TokenStream createSubStream(@NonNull Frame start, @NonNull Frame end) {
        List<Token> tokens = this.tokens.subList(
                start.getTokenId(),
                end.getTokenId() - 1
        );
        return new DefaultTokenStream(file, tokens, true);
    }

    @NonNull
    @Override
    public List<Token> copy() {
        return new ArrayList<>(this.tokens);
    }

    public void tokenize() {
        this.tokens.add(tokenizer.tokenize());
        processed = tokenizer.isProcessed();
    }
}