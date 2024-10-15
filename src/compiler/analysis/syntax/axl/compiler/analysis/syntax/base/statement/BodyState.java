package axl.compiler.analysis.syntax.base.statement;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.base.Node;
import axl.compiler.analysis.syntax.base.State;
import axl.compiler.analysis.syntax.base.StateController;
import axl.compiler.analysis.syntax.base.statement.ast.IfStatement;
import axl.compiler.analysis.syntax.base.statement.ast.ReturnStatement;
import axl.compiler.analysis.syntax.base.statement.ast.WhileStatement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class BodyState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final List<Node> result;

    public BodyState(DefaultSyntaxAnalyzer analyzer, @NotNull List<Node> result) {
        this.analyzer = analyzer;
        this.result = result;
    }

    private boolean start = true;
    private boolean single = true;

    @Override
    public void analyze() {
        if (start) {
            start = false;
            if (analyzer.boolEat(TokenType.LEFT_BRACE)) {
                single = false;
            } else {
                analyzer.getStates().pop();
            }
        }

        if (analyzer.boolEat(TokenType.SEMI))
            return;

        analyzer.checkEnd();
        if (!single && analyzer.boolEat(TokenType.RIGHT_BRACE)) {
            analyzer.getStates().pop();
            return;
        }

        //  TODO add statements
        if (analyzer.boolEat(TokenType.RETURN)) {
            StateController.expression(analyzer, expression -> result.add(new ReturnStatement(expression)));
        } else if (analyzer.boolEat(TokenType.IF)) {
            ifStatement();
        } else if (analyzer.boolEat(TokenType.WHILE)) {
            whileStatement();
        } else {
            StateController.expression(analyzer, result::add);
        }
    }

    private void ifStatement() {
        IfStatement ifStatement = new IfStatement();

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(ifStatement);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (analyzer.boolEat(TokenType.ELSE)) {
                StateController.body(analyzer, ifStatement::setElseBody);
            } else {
                ifStatement.setElseBody(new ArrayList<>());
            }
        });
        StateController.body(analyzer, ifStatement::setBody);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            analyzer.eat(TokenType.RIGHT_PARENT);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            analyzer.eat(TokenType.LEFT_PARENT);
            StateController.expression(analyzer, ifStatement::setExpression);
        });
    }

    private void whileStatement() {
        WhileStatement whileStatement = new WhileStatement();

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(whileStatement);
        });
        StateController.body(analyzer, whileStatement::setBody);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            analyzer.eat(TokenType.RIGHT_PARENT);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            analyzer.eat(TokenType.LEFT_PARENT);
            StateController.expression(analyzer, whileStatement::setExpression);
        });
    }
}
