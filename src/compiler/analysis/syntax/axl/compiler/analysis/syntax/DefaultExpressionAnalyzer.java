package axl.compiler.analysis.syntax;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenGroup;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.expression.*;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EmptyStackException;
import java.util.Stack;

@Getter
@NoArgsConstructor
public class DefaultExpressionAnalyzer {

    private final Stack<OperatorEntry> operatorEntries = new Stack<>();
    private final Stack<Expression> expressions = new Stack<>();
    private Boolean lastExpression = false;

    public final Expression analyze(TokenStream stream) {
        while (stream.hasNext()) {
            if (stream.get().getType().getGroup() == TokenGroup.OPERATOR || stream.get().getType().getGroup() == TokenGroup.DELIMITER) {
                OperatorEntry entry = findOperator(stream.getFile(), stream.next());
                if (entry.getOperator().operator != TokenType.RIGHT_PARENT) {
                    reduce(entry.getOperator().getPriority());
                    operatorEntries.push(entry);
                    lastExpression = false;
                } else {
                    entry.accept();
                    lastExpression = true;
                }
            } else if (findPrimary(stream)) {
                lastExpression = true;
            } else
                throw new RuntimeException();
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

        return true;
    }

    private OperatorEntry findOperator(IFile file, Token token) {
        for (Operator operator : Operator.values()) {
            if (operator.operator == token.getType()) {
                return new OperatorEntry(operator, token);
            }
        }

        throw new IllegalSyntaxException("Unknown operator: " + token.getType(), file, token);
    }

    private void reduce(int priority) {
        while (!operatorEntries.isEmpty() && priority <= operatorEntries.peek().getOperator().getPriority() && operatorEntries.peek().getOperator().getOperator() != TokenType.LEFT_PARENT) {
            operatorEntries.peek().accept();
        }
    }

    private void reduce() {
        while (!operatorEntries.isEmpty())
            operatorEntries.peek().accept();
    }

    @Getter
    private enum Operator {
        RIGHT_PARENT(TokenType.RIGHT_PARENT, (analyzer, token) -> {
            while (!analyzer.getOperatorEntries().isEmpty() && analyzer.getOperatorEntries().peek().getOperator().getOperator() != TokenType.LEFT_PARENT)
                analyzer.getOperatorEntries().peek().accept();

            if (analyzer.getOperatorEntries().isEmpty())
                throw new EmptyStackException();

            if(analyzer.getOperatorEntries().pop().getOperator().operator != TokenType.LEFT_PARENT)
                throw new RuntimeException();
        }, 0),
        LEFT_PARENT(TokenType.LEFT_PARENT, (analyzer, token) -> {
                throw new RuntimeException();
            }, 15),

        ASSIGN(TokenType.ASSIGN, new BinaryGenerator(TokenType.ASSIGN), -1),
        PLUS_ASSIGN(TokenType.PLUS_ASSIGN, new BinaryGenerator(TokenType.PLUS_ASSIGN), -1),
        MINUS_ASSIGN(TokenType.MINUS_ASSIGN, new BinaryGenerator(TokenType.MINUS_ASSIGN), -1),
        MULTIPLY_ASSIGN(TokenType.MULTIPLY_ASSIGN, new BinaryGenerator(TokenType.MULTIPLY_ASSIGN), -1),
        DIVIDE_ASSIGN(TokenType.DIVIDE_ASSIGN, new BinaryGenerator(TokenType.DIVIDE_ASSIGN), -1),
        MODULO_ASSIGN(TokenType.MODULO_ASSIGN, new BinaryGenerator(TokenType.MODULO_ASSIGN), -1),
        EQUALS(TokenType.EQUALS, new BinaryGenerator(TokenType.EQUALS), 0),
        NOT_EQUALS(TokenType.NOT_EQUALS, new BinaryGenerator(TokenType.NOT_EQUALS), 0),
        GREATER(TokenType.GREATER, new BinaryGenerator(TokenType.GREATER), 0),
        LESS(TokenType.LESS, new BinaryGenerator(TokenType.LESS), 0),
        GREATER_OR_EQUAL(TokenType.GREATER_OR_EQUAL, new BinaryGenerator(TokenType.GREATER_OR_EQUAL), 0),
        LESS_OR_EQUAL(TokenType.LESS_OR_EQUAL, new BinaryGenerator(TokenType.LESS_OR_EQUAL), 0),
        ADD(TokenType.PLUS, new BinaryGenerator(TokenType.PLUS), 1),
        SUB(TokenType.MINUS, new BinaryGenerator(TokenType.MINUS), 1),
        MULTI(TokenType.MULTIPLY, new BinaryGenerator(TokenType.MULTIPLY), 2),
        DIV(TokenType.DIVIDE, new BinaryGenerator(TokenType.DIVIDE), 2),
        MOD(TokenType.MODULO, new BinaryGenerator(TokenType.MODULO), 2),
        BIT_AND(TokenType.BIT_AND, new BinaryGenerator(TokenType.BIT_AND), 3),
        BIT_OR(TokenType.BIT_OR, new BinaryGenerator(TokenType.BIT_OR), 4),
        AND(TokenType.AND, new BinaryGenerator(TokenType.AND), 5),
        OR(TokenType.OR, new BinaryGenerator(TokenType.OR), 6),
        UNARY_MINUS(TokenType.UNARY_MINUS, new PrefixUnaryGenerator(TokenType.UNARY_MINUS), 7),
        NOT(TokenType.NOT, new PrefixUnaryGenerator(TokenType.NOT), 7),
        BIT_NOT(TokenType.BIT_NOT, new PrefixUnaryGenerator(TokenType.BIT_NOT), 7),
        BIT_XOR(TokenType.BIT_XOR, new BinaryGenerator(TokenType.BIT_XOR), 7),
        BIT_SHIFT_LEFT(TokenType.BIT_SHIFT_LEFT, new BinaryGenerator(TokenType.BIT_SHIFT_LEFT), 8),
        BIT_SHIFT_RIGHT(TokenType.BIT_SHIFT_RIGHT, new BinaryGenerator(TokenType.BIT_SHIFT_RIGHT), 8),
        ACCESS(TokenType.DOT, new BinaryGenerator(TokenType.DOT), 9),
        IS(TokenType.IS, new BinaryGenerator(TokenType.IS), 9),
        AS(TokenType.AS, new BinaryGenerator(TokenType.AS), 9);
        // QUESTION_MARK(TokenType.QUESTION_MARK, new BinaryGenerator(TokenType.QUESTION_MARK), 11),
      //  INCREMENT(TokenType.INCREMENT, new PrefixUnaryGenerator(TokenType.INCREMENT), 9),
      //  DECREMENT(TokenType.DECREMENT, new PostfixUnaryGenerator(TokenType.DECREMENT), 9),

        private final TokenType operator;
        private final int priority;
        private final Generator generator;

        Operator(TokenType operator, Generator generator, int priority) {
            this.operator = operator;
            this.priority = priority;
            this.generator = generator;
        }
    }

    @Getter
    @AllArgsConstructor
    class OperatorEntry {

        private final Operator operator;

        private final Token token;

        private void accept() {
            operator.getGenerator().accept(DefaultExpressionAnalyzer.this, token);
        }

    }
}

@AllArgsConstructor
class BinaryGenerator implements Generator {

    private final TokenType type;

    @Override
    public void accept(DefaultExpressionAnalyzer analyzer, Token token) {
        analyzer.getOperatorEntries().pop();
        try {
            Expression right = analyzer.getExpressions().pop();
            Expression left = analyzer.getExpressions().pop();
            analyzer.getExpressions().push(new Expression.BinaryExpression(left, right, token));
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for binary operator " + type, e);
        }
    }
}

@AllArgsConstructor
class PrefixUnaryGenerator implements Generator {

    private final TokenType type;

    @Override
    public void accept(DefaultExpressionAnalyzer analyzer, Token token) {
        analyzer.getOperatorEntries().pop();
        try {
            Expression value = analyzer.getExpressions().pop();
            analyzer.getExpressions().push(new Expression.UnaryExpression.PrefixExpression(value, token));
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for prefix unary operator " + type, e);
        }
    }
}


@AllArgsConstructor
class UnaryGenerator implements Generator {

    private final TokenType type;

    public void accept(DefaultExpressionAnalyzer analyzer, Token token) {
        analyzer.getOperatorEntries().pop();
        try {
            Expression value = analyzer.getExpressions().pop();
            analyzer.getExpressions().push(new Expression.UnaryExpression.PostfixExpression(value, token));
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for postfix unary operator " + type, e);
        }
    }
}

interface Generator {

    void accept(DefaultExpressionAnalyzer defaultExpressionAnalyzer, Token token);

}
