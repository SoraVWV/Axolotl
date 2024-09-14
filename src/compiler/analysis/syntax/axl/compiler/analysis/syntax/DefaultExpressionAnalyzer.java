package axl.compiler.analysis.syntax;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenGroup;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultExpressionAnalyzer.OperatorEntry.OperatorType;
import axl.compiler.analysis.syntax.ast.expression.*;
import axl.compiler.analysis.syntax.utils.AnalyzerEntry;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EmptyStackException;
import java.util.Stack;

@Getter
@NoArgsConstructor
public class DefaultExpressionAnalyzer {

    private final Stack<AnalyzerEntry> operatorEntries = new Stack<>();

    private final Stack<Expression> expressions = new Stack<>();

    @Setter
    private Boolean lastExpression = false;

    public final Expression analyze(TokenStream stream) {
        while (stream.hasNext()) {
            if (stream.get().getType().getGroup() == TokenGroup.OPERATOR || stream.get().getType().getGroup() == TokenGroup.DELIMITER) {
                OperatorEntry entry = findOperator(stream.getFile(), stream.next());

                if (entry.getOperator().operator == TokenType.RIGHT_PARENT) {
                    entry.accept();
                    continue;
                }

                reduce(entry.getOperator().getPriority());
                operatorEntries.push(entry);
                lastExpression = false;
            } else if (findPrimary(stream)) {
                lastExpression = true;
            } else {
                throw new RuntimeException();
            }
        }
        reduce();
        if (expressions.size() != 1) {
            throw new RuntimeException();
        }

        return expressions.pop();
    }

    private boolean findPrimary(TokenStream stream) {
        if (stream.get().getType().getGroup() == TokenGroup.LITERAL) {
            expressions.push(new LiteralExpression(stream.next()));
        } else if (stream.get().getType() == TokenType.IDENTIFY) {
            expressions.push(new IdentifyExpression(stream.next()));
        } else if (stream.get().getType() == TokenType.VAR) {
            if (!stream.hasNext())
                throw new RuntimeException();

            stream.next();
            if (!stream.hasNext())
                throw new RuntimeException();

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new RuntimeException();

            expressions.push(new VariableDefineExpression(null, token));
        } else if (stream.get().getType() == TokenType.VAL) {
            if (!stream.hasNext())
                throw new RuntimeException();

            stream.next();
            if (!stream.hasNext())
                throw new RuntimeException();

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new RuntimeException();

            expressions.push(new ValueDefineExpression(null, token));
        } else {
            return false;
        }

        setLastExpression(true);
        return true;
    }

    private OperatorEntry findOperator(IFile file, Token token) {
        for (Operator operator : Operator.values()) {
            if (operator.operator == token.getType()) {
                if (operator.getGenerator() == OperatorGenerator.UNARY) {
                    if (lastExpression)
                        return new OperatorEntry(operator, token, OperatorType.POSTFIX);
                    else
                        return new OperatorEntry(operator, token, OperatorType.PREFIX);
                }
                if (operator.getGenerator() == OperatorGenerator.PREFIX)
                    return new OperatorEntry(operator, token, OperatorType.PREFIX);

                return new OperatorEntry(operator, token, OperatorType.BINARY);
            }
        }

        throw new IllegalSyntaxException("Unknown operator: " + token.getType(), file, token);
    }

    private void reduce(int priority) {
        while (!operatorEntries.isEmpty() &&
                operatorEntries.peek() instanceof OperatorEntry &&
                priority <= ((OperatorEntry) operatorEntries.peek()).getOperator().getPriority() &&
                ((OperatorEntry) operatorEntries.peek()).getOperator().getOperator() != TokenType.LEFT_PARENT) {
            operatorEntries.peek().accept();
        }
    }

    private void reduce() {
        while (!operatorEntries.isEmpty() && operatorEntries.peek() instanceof OperatorEntry)
            operatorEntries.peek().accept();
    }

    @Getter
    private enum Operator {
        RIGHT_PARENT(       TokenType.RIGHT_PARENT,         OperatorGenerator.PARENT,       0),
        LEFT_PARENT(        TokenType.LEFT_PARENT,          OperatorGenerator.EXCEPTION,    15),
        ASSIGN(             TokenType.ASSIGN,               OperatorGenerator.BINARY,       -1),
        PLUS_ASSIGN(        TokenType.PLUS_ASSIGN,          OperatorGenerator.BINARY,       -1),
        MINUS_ASSIGN(       TokenType.MINUS_ASSIGN,         OperatorGenerator.BINARY,       -1),
        MULTIPLY_ASSIGN(    TokenType.MULTIPLY_ASSIGN,      OperatorGenerator.BINARY,       -1),
        DIVIDE_ASSIGN(      TokenType.DIVIDE_ASSIGN,        OperatorGenerator.BINARY,       -1),
        MODULO_ASSIGN(      TokenType.MODULO_ASSIGN,        OperatorGenerator.BINARY,       -1),
        EQUALS(             TokenType.EQUALS,               OperatorGenerator.BINARY,       0),
        NOT_EQUALS(         TokenType.NOT_EQUALS,           OperatorGenerator.BINARY,       0),
        GREATER(            TokenType.GREATER,              OperatorGenerator.BINARY,       0),
        LESS(               TokenType.LESS,                 OperatorGenerator.BINARY,       0),
        GREATER_OR_EQUAL(   TokenType.GREATER_OR_EQUAL,     OperatorGenerator.BINARY,       0),
        LESS_OR_EQUAL(      TokenType.LESS_OR_EQUAL,        OperatorGenerator.BINARY,       0),
        ADD(                TokenType.PLUS,                 OperatorGenerator.BINARY,       1),
        SUB(                TokenType.MINUS,                OperatorGenerator.BINARY,       1),
        MULTI(              TokenType.MULTIPLY,             OperatorGenerator.BINARY,       2),
        DIV(                TokenType.DIVIDE,               OperatorGenerator.BINARY,       2),
        MOD(                TokenType.MODULO,               OperatorGenerator.BINARY,       2),
        BIT_AND(            TokenType.BIT_AND,              OperatorGenerator.BINARY,       3),
        BIT_OR(             TokenType.BIT_OR,               OperatorGenerator.BINARY,       4),
        AND(                TokenType.AND,                  OperatorGenerator.BINARY,       5),
        OR(                 TokenType.OR,                   OperatorGenerator.BINARY,       6),
        UNARY_MINUS(        TokenType.UNARY_MINUS,          OperatorGenerator.PREFIX,       7),
        NOT(                TokenType.NOT,                  OperatorGenerator.PREFIX,       7),
        BIT_NOT(            TokenType.BIT_NOT,              OperatorGenerator.PREFIX,       7),
        BIT_XOR(            TokenType.BIT_XOR,              OperatorGenerator.BINARY,       7),
        BIT_SHIFT_LEFT(     TokenType.BIT_SHIFT_LEFT,       OperatorGenerator.BINARY,       8),
        BIT_SHIFT_RIGHT(    TokenType.BIT_SHIFT_RIGHT,      OperatorGenerator.BINARY,       8),
        ACCESS(             TokenType.DOT,                  OperatorGenerator.BINARY,       9),
        IS(                 TokenType.IS,                   OperatorGenerator.BINARY,       9),
        AS(                 TokenType.AS,                   OperatorGenerator.BINARY,       9),
        INCREMENT(          TokenType.INCREMENT,            OperatorGenerator.UNARY,        9),
        DECREMENT(          TokenType.DECREMENT,            OperatorGenerator.UNARY,        9);
        // QUESTION_MARK(TokenType.QUESTION_MARK, new BinaryGenerator(TokenType.QUESTION_MARK), 11),

        private final TokenType operator;
        private final int priority;
        private final OperatorGenerator generator;

        Operator(TokenType operator, OperatorGenerator generator, int priority) {
            this.operator = operator;
            this.priority = priority;
            this.generator = generator;
        }

    }

    @Getter
    @AllArgsConstructor
    private enum OperatorGenerator {
        BINARY((analyzer, token, type) -> {
            analyzer.getOperatorEntries().pop();
            try {
                Expression right = analyzer.getExpressions().pop();
                Expression left = analyzer.getExpressions().pop();
                analyzer.getExpressions().push(new Expression.BinaryExpression(left, right, token));
                analyzer.setLastExpression(true);
            } catch (EmptyStackException e) {
                throw new IllegalStateException("Invalid expression: insufficient operands for binary operator " + type, e);
            }
        }),
        UNARY((analyzer, token, type) -> {
            analyzer.getOperatorEntries().pop();
            try {
                Expression value = analyzer.getExpressions().pop();
                if (type == OperatorType.POSTFIX)
                    analyzer.getExpressions().push(new Expression.UnaryExpression.PostfixExpression(value, token));
                else
                    analyzer.getExpressions().push(new Expression.UnaryExpression.PrefixExpression(value, token));
                analyzer.setLastExpression(true);
            } catch (EmptyStackException e) {
                throw new IllegalStateException("Invalid expression: insufficient operands for postfix unary operator " + type, e);
            }
        }),
        PREFIX((analyzer, token, type) -> {
            analyzer.getOperatorEntries().pop();
            try {
                Expression value = analyzer.getExpressions().pop();
                analyzer.getExpressions().push(new Expression.UnaryExpression.PrefixExpression(value, token));
                analyzer.setLastExpression(true);
            } catch (EmptyStackException e) {
                throw new IllegalStateException("Invalid expression: insufficient operands for prefix unary operator " + type, e);
            }
        }),
        PARENT((analyzer, token, type) -> {
            while (!analyzer.getOperatorEntries().isEmpty() && ((OperatorEntry) analyzer.getOperatorEntries().peek()).getOperator().getOperator() != TokenType.LEFT_PARENT)
                analyzer.getOperatorEntries().peek().accept();

            if (analyzer.getOperatorEntries().isEmpty())
                throw new EmptyStackException();

            if(((OperatorEntry) analyzer.getOperatorEntries().pop()).getOperator().operator != TokenType.LEFT_PARENT)
                throw new RuntimeException();

            analyzer.setLastExpression(true);
        }), // TODO output
        EXCEPTION(((analyzer, token, type) -> {
            throw new RuntimeException();
        })); // TODO output

        private final Generator lambda;

        interface Generator {

            void accept(DefaultExpressionAnalyzer defaultExpressionAnalyzer, Token token, OperatorType type);
        }
    }

    @Getter
    @AllArgsConstructor
    class OperatorEntry implements AnalyzerEntry {

        private final Operator operator;

        private final Token token;

        private final OperatorType type;

        @Override
        public void accept() {
            operator.getGenerator().getLambda().accept(DefaultExpressionAnalyzer.this, token, type);
        }

        enum OperatorType {
            BINARY,
            PREFIX,
            POSTFIX
        }
    }
}
