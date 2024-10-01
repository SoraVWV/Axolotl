package axl.compiler.analysis.syntax.state.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public final class VariableDefineExpression extends Expression {

    @Nullable
    private final Type type;

    @NotNull
    private final Token name;

    @Override
    public String toString() {
        return "VariableDefineExpression {" +
                "name=" + name.getType() +
                '}';
    }
}
