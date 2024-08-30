package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import lombok.Getter;

public abstract class Analyzer {

    {
        SubAnalyzer subAnalyzer = this.getClass().getAnnotation(SubAnalyzer.class);
        if (subAnalyzer == null)
            throw new IllegalArgumentException("The analyzer \"" + this.getClass().getName() + "\" must have an annotation \"" + SubAnalyzer.class.getName() + "\"!");

        this.target = subAnalyzer.target();
        this.allowed = subAnalyzer.allowed();
    }

    @Getter
    private final Class<? extends Node> target;

    @Getter
    private final Class<? extends Node>[] allowed;

    public abstract Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream);

    public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
        if (getTarget().isAssignableFrom(Expression.class))
            return null;

        return (Expression) analyze(syntaxAnalyzer, tokenStream);
    }
}
