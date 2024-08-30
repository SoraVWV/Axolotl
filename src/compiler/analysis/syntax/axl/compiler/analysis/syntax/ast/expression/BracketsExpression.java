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
public class BracketsExpression extends Expression {

    private final Expression expression;

    @SubAnalyzer(target = BracketsExpression.class)
    public static class BracketsExpressionAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            if (tokenStream.next().getType() == TokenType.LEFT_PARENT) {
                Expression expression = syntaxAnalyzer.analyzeExpression(tokenStream, null);
                if (tokenStream.next().getType() != TokenType.RIGHT_PARENT)
                    throw new RuntimeException(); // todo: уведомить пользователя о том ,что он забыл закрыть скобку
                return new BracketsExpression(expression);
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "BracketsExpression{"+
                "expression="+expression+
                '}';
    }
}
