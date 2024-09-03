package axl.compiler.analysis.syntax.ast;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

        private List<Token> location;

        private boolean all;

    }

    @Getter
    @AllArgsConstructor
    public static class Function {

        private Token name;

        // TODO

    }

}
