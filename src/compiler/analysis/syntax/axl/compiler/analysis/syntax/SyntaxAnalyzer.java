package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.ast.Node;
import lombok.NonNull;

public interface SyntaxAnalyzer {

    @NonNull File analyze(TokenStream tokenStream);

}
