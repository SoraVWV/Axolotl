package axl.compiler.analysis.lexical;

import axl.compiler.IFile;
import lombok.Getter;

@Getter
public class DefaultToken implements Token {

    private final TokenType type;

    int offset;

    int length;

    int line;

    int column;

    DefaultToken(TokenType type) {
        this.type = type;
    }

    @Override
    public String getContent(IFile file) {
        return file
                .getContent()
                .substring(
                        this.offset,
                        this.offset + this.length
                );
    }
}