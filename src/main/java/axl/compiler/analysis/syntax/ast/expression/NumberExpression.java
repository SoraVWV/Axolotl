package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.IToken;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NumberExpression extends Expression {
    private IToken number;

    @SubAnalyzer(target = NumberExpression.class)
    public static class NumberExpressionAnalyzer extends Analyzer {
        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            return switch (tokenStream.get().getType()) {
                case DEC_NUMBER, BIN_LONG_NUMBER, BIN_NUMBER, DEC_LONG_NUMBER, DOUBLE_EXP_NUMBER, DOUBLE_NUMBER, FLOAT_EXP_NUMBER, FLOAT_NUMBER, HEX_LONG_NUMBER, HEX_NUMBER -> {
                    IToken token = tokenStream.next();
                    yield new NumberExpression(token);
                }
                default -> null;
            };
        }
    }

    @Override
    public String toString() {
        return "NumberExpression{" +
                "number=" + number.getType() +
                '}';
    }
}
