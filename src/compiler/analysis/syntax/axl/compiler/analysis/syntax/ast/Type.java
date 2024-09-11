package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public final class Type {

    @Nullable
    private final Type superType;

    @NotNull
    private final List<Type> genericTypes = new ArrayList<>();

    @NotNull
    private final Token name;
}
