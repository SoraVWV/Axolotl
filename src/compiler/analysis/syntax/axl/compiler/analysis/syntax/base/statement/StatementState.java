package axl.compiler.analysis.syntax.base.statement;

import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.base.Node;
import axl.compiler.analysis.syntax.base.State;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public class StatementState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final Consumer<Node> result;

    @Override
    public void analyze() {
        analyzer.checkEnd();
        switch (analyzer.getStream().get().getType()) {
            case IF -> {

            }
        }
    }

    private void ifStatement() {

    }
}
