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
public class VariableDefine extends Expression {

    private final Token variable;
    private final Expression value;
    private final Token type;

    @SubAnalyzer(target = VariableDefine.class)
    public static class VariableDefineAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            return switch (tokenStream.get().getType()) {
                case VAL, VAR ->  {
                    tokenStream.next();
                    Token variable = tokenStream.next();
                    yield switch (tokenStream.next().getType()) {
                        case TYPE -> new VariableDefine(variable, null, tokenStream.next());
                        case ASSIGN -> {
                            Expression expression = (Expression) syntaxAnalyzer.analyze(tokenStream);
                            yield new VariableDefine(variable, expression, null);
                        }
                        default -> throw new RuntimeException(); // todo: объяснить пользователю что его выражение неверно
                    };
                }
                default -> null;
            };
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return "VariableDefine{" +
                    "variable=" + variable.getType() +
                    ",type=" + type.getType() +
                    '}';
        } else {
            return "VariableDefine{" +
                    "variable=" + variable.getType() +
                    ",value=" + value +
                    '}';
        }
    }
}
