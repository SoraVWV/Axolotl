package axl.compiler.analysis.lexical;

public record DefaultTokenizerFrame(int offset, int line, int column) implements TokenizerFrame {
}