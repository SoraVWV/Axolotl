package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public final class Argument {

//    @NotNull
//    private final List<Annotation> annotations = new ArrayList<>();

    @NotNull
    private final Type type;

    @NotNull
    private final Token name;

    @Override
    public String toString() {
        return "Argument{" +
                "type=" + type +
                ",name=" + name.getType() +
                '}';
    }
}
