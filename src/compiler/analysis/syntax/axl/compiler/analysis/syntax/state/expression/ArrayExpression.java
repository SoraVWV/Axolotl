package axl.compiler.analysis.syntax.state.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class ArrayExpression extends Expression {

    @NotNull
    private final Expression root;

    @NotNull
    private final Expression index;
}
