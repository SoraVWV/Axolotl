package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringExpression extends Expression {
    private Token string;

    @SubAnalyzer(target = StringExpression.class)
    public static class StringExpressionAnalyzer extends Analyzer {
        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            if (tokenStream.get().getType() == TokenType.STRING_LITERAL) {
                Token token = tokenStream.next();
                return new StringExpression(token);
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "StringExpression{" +
                "string=" + string.getType() +
                '}';
    }
}
