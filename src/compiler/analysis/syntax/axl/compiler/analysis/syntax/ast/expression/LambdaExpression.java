package axl.compiler.analysis.syntax.ast.expression;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.ast.Annotation;
import axl.compiler.analysis.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public final class LambdaExpression extends Expression {

    @NotNull
    private final List<LambdaArgument> arguments = new ArrayList<>();

    @NotNull
    private final List<Expression> body = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public static final class LambdaArgument {

        @NotNull
        private final List<Annotation> annotations = new ArrayList<>();

        @Nullable
        private final Type type;

        @NotNull
        private final Token name;
    }
}

