package axl.compiler.analysis.syntax.state.expression;

import axl.compiler.analysis.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class CastExpression extends Expression {

    @NotNull
    private final Type type;

    @NotNull
    private final Expression value;
}
