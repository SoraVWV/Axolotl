package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.base.Node;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.base.declaration.FileState;
import axl.compiler.analysis.syntax.base.State;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer<File> {

    private final Stack<Node> nodes = new Stack<>();

    private final Stack<Object> context = new Stack<>();

    private final Stack<State> states = new Stack<>();

    private final TokenStream stream;

    public DefaultSyntaxAnalyzer(TokenStream stream) {
        this.stream = stream;
    }

    @SneakyThrows
    @Override
    public @NonNull File analyze() {
        FileState fileState = new FileState(this);
        states.push(fileState);

        while(!states.isEmpty() && stream.hasNext()) {
//            Thread.sleep(100);
//            System.out.println(Arrays.toString(getStates().toArray()));
//            System.out.println(Arrays.toString(getNodes().toArray()));
//            System.out.println();
            states.peek().analyze();
        }

        if (stream.hasNext())
            throw new RuntimeException();

        return fileState.build();
    }

    private List<Expression> expr = new ArrayList<>();
}

