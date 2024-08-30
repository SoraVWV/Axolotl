package axl.compiler.analysis.syntax.ast.statement;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Block extends Statement {

    List<Statement> statements;

    @SubAnalyzer(target = Block.class)
    public static class BlockAnalyzer extends Analyzer {

        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            List<Statement> statements = new ArrayList<>();

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() != TokenType.LEFT_BRACE)
                return null;

            for(;;) {
                if (!tokenStream.hasNext())
                    throw new RuntimeException();

                if (tokenStream.get().getType() == TokenType.RIGHT_BRACE) {
                    tokenStream.next();
                    break;
                }

                statements.add((Statement) syntaxAnalyzer.analyze(tokenStream, Statement.class));
            }

            return new Block(statements);
        }
    }

}
