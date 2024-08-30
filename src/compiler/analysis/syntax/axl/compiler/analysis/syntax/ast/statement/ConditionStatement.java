package axl.compiler.analysis.syntax.ast.statement;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ConditionStatement extends Statement {

    @NonNull
    private final Expression condition;

    @NonNull
    private final Statement bodyIfConditionTrue;

    private final Statement bodyIfConditionFalse;

    @SubAnalyzer(target = ConditionStatement.class)
    public static class ConditionAnalyzer extends Analyzer {

        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            Expression condition;
            Statement bodyIfConditionTrue;
            Statement bodyIfConditionFalse;
            {
                Token ifKeyword = tokenStream.next();
                if (ifKeyword == null || ifKeyword.getType() != TokenType.IF)
                    return null;
            }
            {
                Token leftParent = tokenStream.next();
                if (leftParent == null || leftParent.getType() != TokenType.LEFT_PARENT)
                    return null;
            }
            {
                condition = (Expression) syntaxAnalyzer.analyze(tokenStream, Expression.class);
                if (condition == null)
                    return null;
            }
            {
                Token rightParent = tokenStream.next();
                if (rightParent == null || rightParent.getType() != TokenType.RIGHT_PARENT)
                    return null;
            }
            {
                bodyIfConditionTrue = (Statement) syntaxAnalyzer.analyze(tokenStream, Statement.class);
                if (bodyIfConditionTrue == null)
                    return null;
            }
            {
                Token elseKeyword = tokenStream.next();
                if (elseKeyword == null || elseKeyword.getType() != TokenType.ELSE)
                    return new ConditionStatement(condition, bodyIfConditionTrue, null);
            }
            {
                bodyIfConditionFalse = (Statement) syntaxAnalyzer.analyze(tokenStream, Statement.class);
                if (bodyIfConditionFalse == null)
                    return null;
            }
            return new ConditionStatement(condition, bodyIfConditionTrue, bodyIfConditionFalse);
        }
    }

}