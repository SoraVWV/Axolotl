package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
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
public class AssigmentExpression extends Expression {

    private final Token operation;
    private final Expression left;
    private final Expression right;

    @SubAnalyzer(target = BinaryExpression.class)
    public static class AssigmentExpressionAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(without, this));
            if (left == null)
                return null;
            Token token = tokenStream.get();
            if (token == null)
                return left;
            return switch (token.getType()) {
                case ASSIGN,
                     PLUS_ASSIGN,
                     MINUS_ASSIGN,
                     MULTIPLY_ASSIGN,
                     DIVIDE_ASSIGN,
                     MODULO_ASSIGN,
                     BIT_AND_ASSIGN,
                     BIT_OR_ASSIGN,
                     BIT_XOR_ASSIGN,
                     BIT_SHIFT_LEFT_ASSIGN,
                     BIT_SHIFT_RIGHT_ASSIGN -> {
                    Token operation = tokenStream.next();
                    Expression right = syntaxAnalyzer.analyzeExpression(tokenStream, without);
                    yield new BinaryExpression(operation, left, right);
                }
                default -> left;
            };
        }
    }

    @Override
    public String toString() {
        return "AssigmentExpression{"+
                "operation="+operation.getType()+
                ",left="+left+
                ",right="+right+
                '}';
    }
}
