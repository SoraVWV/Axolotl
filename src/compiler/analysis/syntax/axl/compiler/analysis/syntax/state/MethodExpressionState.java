package axl.compiler.analysis.syntax.state;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.state.expression.Expression;
import axl.compiler.analysis.syntax.state.expression.MethodExpression;

public class MethodExpressionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final MethodExpression method;

    private final int level;

    public MethodExpressionState(DefaultSyntaxAnalyzer analyzer, MethodExpression method) {
        this.analyzer = analyzer;
        this.method = method;
        analyzer.getExpressions().push(method);
        this.level = analyzer.getExpressions().size();
    }

    @Override
    public void analyze() {
        final TokenStream stream = analyzer.getStream();
        if (!stream.hasNext())
            throw new RuntimeException();

        if (analyzer.getExpressions().size() == level + 1) {
            Expression expression = analyzer.getExpressions().pop();
            ((MethodExpression) analyzer.getExpressions().peek()).getArguments().add(expression);
        }

        if (stream.get().getType() == TokenType.RIGHT_PARENT) {
            stream.next();
            analyzer.getStates().pop();
            return;
        }

        if (method.getArguments().isEmpty()) {
            analyzer.getStates().push(new ExpressionState(analyzer));
            return;
        }

        if (stream.get().getType() == TokenType.COMMA) {
            stream.next();
            analyzer.getStates().push(new ExpressionState(analyzer));
            return;
        }

        throw new RuntimeException();
    }
}
