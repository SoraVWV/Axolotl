package axl.compiler.analysis.syntax.ast;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@Getter
@NoArgsConstructor
public final class Annotation {

    @NotNull
    private final HashMap<String, Annotation> annotations = new HashMap<>();

    @NotNull
    private final HashMap<String, List<Annotation>> annotationsList = new HashMap<>();
}
