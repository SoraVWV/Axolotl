package axl.compiler.analysis.lexical;

import axl.compiler.IFile;
import lombok.NonNull;

public interface Token {

    int getOffset();

    int getLength();

    int getLine();

    int getColumn();

    @NonNull
    TokenType getType();

    @NonNull
    String getContent(IFile file);
}
