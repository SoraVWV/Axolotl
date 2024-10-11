package axl.compiler.analysis.syntax.base.statement;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.base.Node;
import axl.compiler.analysis.syntax.base.State;
import axl.compiler.analysis.syntax.base.StateController;
import axl.compiler.analysis.syntax.base.statement.ast.ReturnStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BodyState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final List<Node> result;

    public BodyState(DefaultSyntaxAnalyzer analyzer, @NotNull List<Node> result) {
        this.analyzer = analyzer;
        this.result = result;
    }

    private boolean start = true;

    @Override
    public void analyze() {
        if (start) {
            start = false;
            analyzer.eat(TokenType.LEFT_BRACE);
        }

        analyzer.checkEnd();
        if (analyzer.check(TokenType.RIGHT_BRACE)) {
            analyzer.eat(TokenType.RIGHT_BRACE);
            analyzer.getStates().pop();
            return;
        }

        //  TODO check statements
        if (analyzer.boolEat(TokenType.RETURN)) {
            StateController.expression(analyzer, expression -> result.add(new ReturnStatement(expression)));
        } else {
            StateController.expression(analyzer, result::add);
        }
    }
}
