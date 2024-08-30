package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.utils.Frame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.ExpressionAnalyzer;
import axl.compiler.analysis.syntax.utils.LinkedList;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnaryExpression extends Expression {

    private final boolean prefix;
    private final Token operation;
    private final Expression expression;

    @SubAnalyzer(target = UnaryExpression.class)
    public static class UnaryExpressionIncrementDecrementAnalyzer extends ExpressionAnalyzer {

        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Frame frame = tokenStream.createFrame();
            Expression expression = prefix(syntaxAnalyzer, tokenStream);
            if (expression == null) {
                tokenStream.restoreFrame(frame);
                expression = postfix(syntaxAnalyzer, tokenStream);
            }

            return expression;
        }

        private Expression prefix(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            Token token = tokenStream.get();
            if (token == null)
                return null;

            return switch (token.getType()) {
                case INCREMENT, DECREMENT -> {
                    tokenStream.next();
                    Expression right = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(null, this));
                    if (right == null)
                        yield null;

                    yield new UnaryExpression(true, token, right);
                }
                default -> null;
            };
        }

        private Expression postfix(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(null, this));
            if (left == null)
                return null;

            Token token = tokenStream.get();
            if (token == null)
                return left;

            return switch (token.getType()) {
                case INCREMENT, DECREMENT -> {
                    tokenStream.next();
                    yield new UnaryExpression(false, token, left);
                }
                default -> left;
            };
        }

    }

    @SubAnalyzer(target = UnaryExpression.class)
    public static class UnaryExpressionPrefixAnalyzer extends ExpressionAnalyzer {

        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Token token = tokenStream.get();
            if (token == null)
                return null;

            return switch (token.getType()) {
                case NOT, BIT_NOT, UNARY_MINUS -> {
                    tokenStream.next();
                    Expression right = syntaxAnalyzer.analyzeExpression(tokenStream, null);
                    if (right == null)
                        yield null;

                    yield new UnaryExpression(true, token, right);
                }
                default -> null;
            };
        }
    }

    @Override
    public String toString() {
        return "UnaryExpression{"+
                "prefix="+prefix+
                ",operation="+operation.getType()+
                ",expression="+expression+
                '}';
    }
}
