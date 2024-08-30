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

@Getter
@AllArgsConstructor
public class InstanceAccessExpression extends Expression {
    private final Expression instance;
    private final Token access;

    @SubAnalyzer(target = InstanceAccessExpression.class)
    public static class InstanceAccessExpressionAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(without, this));
            if (tokenStream.nextTokenTypeNot(TokenType.DOT))
                return null;
            Token access = tokenStream.next();
            return new InstanceAccessExpression(left, access);
        }
    }

    @Override
    public String toString() {
        return "InstanceAccessExpression{"+
                "instance="+instance+
                ",access="+access.getType()+
                '}';
    }
}
