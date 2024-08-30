package axl.compiler.analysis.syntax.ast.statement;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnStatement extends Statement {

    private Expression result;

    @SubAnalyzer(
            target = ReturnStatement.class
    )
    public static class ReturnAnalyzer extends Analyzer {

        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() != TokenType.RETURN)
                return null;

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() == TokenType.SEMI)
                return new ReturnStatement(null);

            Expression expression = (Expression) syntaxAnalyzer.analyze(tokenStream, Expression.class);
            if (expression == null)
                return null;

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() == TokenType.SEMI)
                return new ReturnStatement(expression);

            return null;
        }
    }

}
