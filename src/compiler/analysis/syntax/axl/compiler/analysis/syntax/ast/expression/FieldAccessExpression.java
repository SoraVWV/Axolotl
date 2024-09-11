package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class FieldAccessExpression extends Expression {

    @NotNull
    private final Expression root;

    @NotNull
    private final Token name;
}
