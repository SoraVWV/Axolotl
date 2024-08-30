package axl.compiler.analysis.lexical.utils;

public sealed interface Frame permits DefaultFrame {

    int getTokenId();

}
