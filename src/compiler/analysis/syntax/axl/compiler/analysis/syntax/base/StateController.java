package axl.compiler.analysis.syntax.base;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.base.expression.Expression;
import axl.compiler.analysis.syntax.base.expression.MethodExpression;
import axl.compiler.analysis.syntax.base.expression.ValueDefineExpression;
import axl.compiler.analysis.syntax.base.expression.VariableDefineExpression;
import axl.compiler.analysis.syntax.base.expression.state.ExpressionState;
import axl.compiler.analysis.syntax.base.expression.state.MethodExpressionState;
import axl.compiler.analysis.syntax.ast.Type;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import axl.compiler.analysis.syntax.utils.StateUtils;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class StateController {

    public static void typeState(DefaultSyntaxAnalyzer analyzer, Consumer<Type> result) {
        analyzer.getStates().push(() -> {
            List<Token> tokens = StateUtils.getLocation(analyzer);
            result.accept(new Type(tokens));
            analyzer.getStates().pop();
        });
    }

    public static void expression(DefaultSyntaxAnalyzer analyzer, Consumer<Expression> result) {
        analyzer.getStates().push(new ExpressionState(analyzer, result));
    }

    public static void method(DefaultSyntaxAnalyzer analyzer, Consumer<MethodExpression> result) {
        analyzer.getStates().push(new MethodExpressionState(analyzer, result));
    }

    public static void var(DefaultSyntaxAnalyzer analyzer, Consumer<VariableDefineExpression> result) {
        analyzer.getStates().push(() -> {
            TokenStream stream = analyzer.getStream();

            stream.next();
            if (!stream.hasNext())
                throw new IllegalSyntaxException("Invalid variable declaration entry", stream);

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new IllegalSyntaxException("Invalid variable declaration entry", stream);

            VariableDefineExpression variableDefineExpression = new VariableDefineExpression(null, token);
            result.accept(variableDefineExpression);

            analyzer.getStates().pop();
            if (stream.hasNext() && stream.get().getType() == TokenType.TYPE) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.typeState(analyzer, variableDefineExpression::setType);
            }
        });
    }

    public static void val(DefaultSyntaxAnalyzer analyzer, Consumer<ValueDefineExpression> result) {
        analyzer.getStates().push(() -> {
            TokenStream stream = analyzer.getStream();

            stream.next();
            if (!stream.hasNext())
                throw new IllegalSyntaxException("Invalid value declaration entry", stream);

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new IllegalSyntaxException("Invalid value declaration entry", stream);

            ValueDefineExpression valueDefineExpression = new ValueDefineExpression(null, token);
            result.accept(valueDefineExpression);

            analyzer.getStates().pop();
            if (stream.hasNext() && stream.get().getType() == TokenType.TYPE) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.typeState(analyzer, valueDefineExpression::setType);
            }
        });
    }
}
