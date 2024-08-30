package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.ExpressionAnalyzer;
import axl.compiler.analysis.syntax.utils.LinkedList;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BinaryExpression extends Expression {

    private final Token operation;
    private final Expression left;
    private final Expression right;

    @SubAnalyzer(target = BinaryExpression.class)
    public static class BinaryExpressionAnalyzer extends ExpressionAnalyzer {
        private final List<TokenType> tokenTypes;

        public BinaryExpressionAnalyzer(List<TokenType> tokenTypes) {
            this.tokenTypes = tokenTypes;
        }

        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(without, this));
            if (left == null)
                return null;
            Token token = tokenStream.get();
            if (token == null || !tokenTypes.contains(token.getType()))
                return left;
            Token operation = tokenStream.next();
            Expression right = syntaxAnalyzer.analyzeExpression(tokenStream, without);
            if (right == null)
                return null;
            return new BinaryExpression(operation, left, right);
        }
    }

    @Override
    public String toString() {
        return "BinaryExpression{"+
                "operation="+operation.getType()+
                ",left="+left+
                ",right="+right+
                '}';
    }
}
