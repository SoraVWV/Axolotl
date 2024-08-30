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

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CallFunctionExpression extends Expression {
    private final Expression function;
    private final List<Expression> args;

    @SubAnalyzer(target = CallFunctionExpression.class)
    public static class CallFunctionExpressionAnalyzer extends ExpressionAnalyzer {
        @Override
        public Expression analyzeExpression(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream, LinkedList<Analyzer> without) {
            Expression left = syntaxAnalyzer.analyzeExpression(tokenStream, new LinkedList<>(without, this));
            if (left == null || tokenStream.nextTokenTypeNot(TokenType.LEFT_PARENT))
                return null;

            List<Expression> args = new ArrayList<>();
            if (tokenStream.get().getType() == TokenType.RIGHT_PARENT)
                tokenStream.next();
            else {
                while (true) {
                    Expression expr = syntaxAnalyzer.analyzeExpression(tokenStream, null);
                    if (expr == null) {
                        throw new RuntimeException(); // TODO
                    }

                    args.add(expr);

                    Token token = tokenStream.next();
                    if (token == null)
                        throw new RuntimeException(); // TODO

                    if (token.getType() == TokenType.COMMA)
                        continue;
                    else if (token.getType() == TokenType.RIGHT_PARENT)
                        break;
                    else
                        throw new RuntimeException("Пропущена запятая или закрывающая скобка"); // TODO
                }
            }
            return new CallFunctionExpression(left, args);
        }
    }

    @Override
    public String toString() {
        return "CallFunction{" +
                "function=" + function +
                ",args=" + args +
                '}';
    }
}
