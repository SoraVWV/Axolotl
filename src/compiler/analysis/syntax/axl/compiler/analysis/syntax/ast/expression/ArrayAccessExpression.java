package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.TokenType;
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
public class ArrayAccessExpression extends Expression {
    private final Expression array;
    private final Expression index;

    @SubAnalyzer(target = ArrayAccessExpression.class)
    public static class ArrayAccessExpressionAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(without, this));
            if (left == null || tokenStream.nextTokenTypeNot(TokenType.LEFT_SQUARE))
                return null;
            Expression index = syntaxAnalyzer.analyzeExpression(tokenStream, without);
            if (tokenStream.next().getType() != TokenType.RIGHT_SQUARE)
                throw new RuntimeException(); // todo: сообщить пользователю о необходимости закрыть скобку
            return new ArrayAccessExpression(left, index);
        }
    }

    @Override
    public String toString() {
        return "ArrayAccessExpression{"+
                "array="+array+
                ",index="+index+
                '}';
    }
}
