package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.state.FileState;
import axl.compiler.analysis.syntax.state.State;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer<File> {

    private final Stack<State> states = new Stack<>();

    private final TokenStream stream;

    public DefaultSyntaxAnalyzer(TokenStream stream) {
        this.stream = stream;
    }

    @Override
    public @NonNull File analyze() {
        FileState fileState = new FileState();
        states.push(fileState);

        while(!states.isEmpty() && stream.hasNext())
            states.peek().analyze();

        if (stream.hasNext())
            throw new RuntimeException();

        return fileState.build();
    }
}

