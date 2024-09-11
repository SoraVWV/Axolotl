package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@AllArgsConstructor
public final class MethodExpression extends Expression {

    @NotNull
    private final Expression root;

    @NotNull
    private final Token name;

    @NotNull
    private final List<Expression> arguments;
}
