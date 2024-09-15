package axl.compiler.analysis.syntax.state.expression;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class IdentifyExpression extends Expression {

    @NotNull
    private final Token value;

    @Override
    public String toString() {
        return "IdentifyExpression {" +
                "value=" + value.getType() +
                '}';
    }
}
