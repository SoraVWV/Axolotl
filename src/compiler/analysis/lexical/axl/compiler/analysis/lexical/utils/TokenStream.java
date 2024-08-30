package axl.compiler.analysis.lexical.utils;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TokenStream {

    @NonNull
    IFile getFile();

    @Nullable
    Token next();

    @Nullable
    Token get();

    boolean hasNext();

    boolean nextTokenTypeIs(TokenType compare);

    boolean nextTokenTypeNot(TokenType compare);

    @NonNull
    Frame createFrame();

    void restoreFrame(@NonNull Frame frame);

    @NonNull
    TokenStream createSubStream(Frame start, Frame end);

    @NonNull
    List<Token> copy();
}
