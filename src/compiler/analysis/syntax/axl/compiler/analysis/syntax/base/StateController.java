package axl.compiler.analysis.syntax.base;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.ast.expression.MethodExpression;
import axl.compiler.analysis.syntax.ast.expression.ValueDefineExpression;
import axl.compiler.analysis.syntax.ast.expression.VariableDefineExpression;
import axl.compiler.analysis.syntax.base.declaration.FunctionState;
import axl.compiler.analysis.syntax.base.expression.ExpressionState;
import axl.compiler.analysis.syntax.base.expression.MethodExpressionState;
import axl.compiler.analysis.syntax.ast.Type;
import axl.compiler.analysis.syntax.base.statement.BodyState;
import axl.compiler.analysis.syntax.base.statement.StatementState;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import axl.compiler.analysis.syntax.utils.StateUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class StateController {

    public static void type(DefaultSyntaxAnalyzer analyzer, Consumer<Type> result) {
        analyzer.getStates().push(() -> {
            List<Token> tokens = StateUtils.getLocation(analyzer);
            result.accept(new Type(tokens));
            analyzer.getStates().pop();
        });
    }

    public static void expression(DefaultSyntaxAnalyzer analyzer, Consumer<Expression> result) {
        analyzer.getStates().push(new ExpressionState(analyzer, result));
    }

    public static void methodCall(DefaultSyntaxAnalyzer analyzer, Consumer<MethodExpression> result) {
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
            analyzer.getStates().pop();
            result.accept(variableDefineExpression);

            if (stream.hasNext() && stream.get().getType() == TokenType.COLON) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.type(analyzer, variableDefineExpression::setType);
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
            if (stream.hasNext() && stream.get().getType() == TokenType.COLON) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.type(analyzer, valueDefineExpression::setType);
            }
        });
    }

    public static void function(DefaultSyntaxAnalyzer analyzer, Consumer<File.Function> result) {
        analyzer.getStates().push(new FunctionState(analyzer, result));
    }

    public static void custom(DefaultSyntaxAnalyzer analyzer, State state) {
        analyzer.getStates().push(state);
    }

    public static void body(DefaultSyntaxAnalyzer analyzer, @NotNull List<Node> body) {
        analyzer.getStates().push(new BodyState(analyzer, body));
    }

    public static void body(DefaultSyntaxAnalyzer analyzer, @NotNull Consumer<List<Node>> result) {
        List<Node> body = new ArrayList<>();
        custom(analyzer, () -> {
            result.accept(body);
            analyzer.getStates().pop();
        });
        analyzer.getStates().push(new BodyState(analyzer, body));
    }

    public static void statement(DefaultSyntaxAnalyzer analyzer, Consumer<Node> result) {
        analyzer.getStates().push(new StatementState(analyzer, result));
    }
}
