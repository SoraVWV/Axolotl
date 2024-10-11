package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public final class VariableDefineExpression extends Expression {

    @Setter
    @Nullable
    private Type type;

    @NotNull
    private final Token name;

    @Override
    public String toString() {
        return "VariableDefineExpression {" +
                "name=" + name.getType() +
                ",type=" + type +
                '}';
    }
}
