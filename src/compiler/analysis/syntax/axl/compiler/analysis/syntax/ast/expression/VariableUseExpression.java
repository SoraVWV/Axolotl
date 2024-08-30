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
public class VariableUseExpression extends Expression {
    private final Token variable;

    @SubAnalyzer(target = VariableUseExpression.class)
    public static class VariableUseAnalyzer extends Analyzer {
        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            if (tokenStream.get().getType() == TokenType.IDENTIFY)
                return new VariableUseExpression(tokenStream.next());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Variable{" +
                "variable=" + variable.getType() +
                '}';
    }
}
