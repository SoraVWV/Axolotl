package axl.compiler.analysis.syntax.state;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.state.expression.Expression;
import axl.compiler.analysis.syntax.state.expression.MethodExpression;

import java.util.function.Consumer;

public class MethodExpressionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final Consumer<Expression> result;

    private final MethodExpression method;

    public MethodExpressionState(DefaultSyntaxAnalyzer analyzer, MethodExpression method, Consumer<Expression> result) {
        this.analyzer = analyzer;
        this.result = result;
        this.method = method;
    }

    @Override
    public void analyze() {
        final TokenStream stream = analyzer.getStream();
        if (!stream.hasNext())
            throw new RuntimeException();

        if (stream.get().getType() == TokenType.RIGHT_PARENT) {
            stream.next();
            analyzer.getStates().pop();
            result.accept(method);
            return;
        }

        if (method.getArguments().isEmpty()) {
            analyzer.getStates().push(new ExpressionState(analyzer, method.getArguments()::add));
            return;
        }

        if (stream.get().getType() == TokenType.COMMA) {
            stream.next();
            analyzer.getStates().push(new ExpressionState(analyzer, method.getArguments()::add));
            return;
        }

        System.out.println(stream.get().getType());
        throw new RuntimeException();
    }
}
