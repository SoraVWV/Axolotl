package axl.compiler.analysis.syntax.state.expression;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class LiteralExpression extends Expression {

    @NotNull
    private final Token value;

    @Override
    public String toString() {
        return "LiteralExpression {" +
                "value=" + value.getType() +
                '}';
    }
}
