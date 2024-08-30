package axl.compiler;

import axl.compiler.analysis.lexical.DefaultTokenizer;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.Tokenizer;
import axl.compiler.analysis.lexical.utils.DefaultTokenStream;
import axl.compiler.analysis.lexical.utils.TokenStream;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// TODO
public class File implements IFile {

    @Getter
    private final String filename;

    @Getter
    private final String content;

    private final List<Token> tokens;

    private final Tokenizer tokenizer;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.tokens = new ArrayList<>();
        this.tokenizer = new DefaultTokenizer(this);
    }

    @Override
    public TokenStream createTokenStream() {
        return new DefaultTokenStream(this, tokens, tokenizer);
    }

}
