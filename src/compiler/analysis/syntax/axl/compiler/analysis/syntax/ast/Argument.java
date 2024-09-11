package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public final class Argument {

    @NotNull
    private final List<Annotation> annotations = new ArrayList<>();

    @NotNull
    private final Type type;

    @NotNull
    private final Token name;
}
