package axl.compiler.analysis.syntax.state.expression.state;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.state.State;
import axl.compiler.analysis.syntax.utils.AnalyzerEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class OperatorEntry implements AnalyzerEntry {

    private final Operator operator;

    private final Token token;

    private final OperatorType type;

    @Override
    public void accept(State state) {
        operator.getGenerator().getLambda().accept(state, token, type);
    }

    enum OperatorType {
        BINARY,
        PREFIX,
        POSTFIX
    }
}