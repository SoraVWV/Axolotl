package axl.compiler.analysis.lexical;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultTokenizerFrame implements TokenizerFrame {

    private final int offset;

    private final int line;

    private final int column;

}