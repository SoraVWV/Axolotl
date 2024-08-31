package axl.compiler.analysis.lexical;

public interface TokenizerFrame {

    int line();

    int column();

    int offset();
}
