package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.utils.TokenStream;
import lombok.NonNull;

public interface SyntaxAnalyzer<T> {

    @NonNull T analyze();

    TokenStream getStream();
}
