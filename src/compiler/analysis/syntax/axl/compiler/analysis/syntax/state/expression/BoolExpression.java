package axl.compiler.analysis.syntax.state.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class BoolExpression extends Expression {

    @NotNull
    private final Boolean value;
}
