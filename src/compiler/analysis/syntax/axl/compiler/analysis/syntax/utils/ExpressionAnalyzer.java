package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.ast.expression.Expression;

public abstract class ExpressionAnalyzer extends Analyzer {

    public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
        return this.analyzeExpression(syntaxAnalyzer, tokenStream, null);
    }

    public abstract Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without);
}
