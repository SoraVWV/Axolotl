package axl.compiler.analysis.syntax.ast;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.syntax.base.Node;
import axl.compiler.analysis.syntax.base.expression.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class File implements Node {

    @Setter
    private IFile file;

    @Setter
    private List<Token> location;

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

        @Override
        public String toString() {
            return "Import {" +
                    "location=" +
                    location.stream().map(Token::toString).collect(Collectors.joining(".")) + (all ? ".*" : "") +
                    "}";
        }
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

    @Override
    public String toString() {
        return "File {" +
                "location=" + String.join(".", location.stream().map(Object::toString).collect(Collectors.joining("."))) +
                ",imports=[" +
                imports.stream().map(Import::toString).collect(Collectors.joining(",")) +
                "]}";
    }
}
