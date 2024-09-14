package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public sealed class Expression permits ArrayExpression, BoolExpression, CastExpression, Expression.BinaryExpression, Expression.UnaryExpression, FieldAccessExpression, IdentifyExpression, LambdaExpression, LiteralExpression, MethodExpression, ValueDefineExpression, VariableDefineExpression {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static final class BinaryExpression extends Expression {

        @NotNull
        private final Expression left;

        @NotNull
        private final Expression right;

        @NotNull
        private final Token operator;

        @Override
        public String toString() {
            return "BinaryExpression {" +
                    "operation=" + operator.getType() +
                    ",left=" + left +
                    ",right=" + right +
                    '}';
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static sealed class UnaryExpression extends Expression {

        @NotNull
        protected final Expression expression;

        @NotNull
        protected final Token operator;

        public static final class PrefixExpression extends UnaryExpression {

            public PrefixExpression(Expression expression, Token operator) {
                super(expression, operator);
            }

            @Override
            public String toString() {
                return "PrefixExpression {" +
                        "operation=" + operator.getType() +
                        ",value=" + expression +
                        '}';
            }
        }

        public static final class PostfixExpression extends UnaryExpression {

            public PostfixExpression(Expression expression, Token operator) {
                super(expression, operator);
            }

            @Override
            public String toString() {
                return "PostfixExpression {" +
                        "operation=" + operator.getType() +
                        ",value=" + expression +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "UnaryExpression {" +
                    "operation=" + operator.getType() +
                    ",value=" + expression +
                    '}';
        }
    }

}