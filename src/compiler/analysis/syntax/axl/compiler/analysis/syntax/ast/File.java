package axl.compiler.analysis.syntax.ast;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class File {

    @Setter
    private IFile file;

    private final List<Import> imports = new ArrayList<>();

    private final List<Function> functions = new ArrayList<>();

    // TODO private List<Class> classes;

    @Getter
    @AllArgsConstructor
    public static class Import {

        @NotNull
        private List<Token> location;

        @NotNull
        private Boolean all;
    }

    @Getter
    @AllArgsConstructor
    public static class Function {

        @NotNull
        private final Token name;

        @NotNull
        private final List<Type> genericTypes = new ArrayList<>();

        @NotNull
        private final List<Argument> arguments = new ArrayList<>();

        @NotNull
        private final List<Expression> body = new ArrayList<>();
    }
}
