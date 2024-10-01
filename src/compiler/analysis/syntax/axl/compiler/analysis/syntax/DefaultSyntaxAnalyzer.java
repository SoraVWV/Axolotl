package axl.compiler.analysis.syntax;

import axl.compiler.Main;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.state.Node;
import axl.compiler.analysis.syntax.state.expression.state.ExpressionState;
import axl.compiler.analysis.syntax.state.declaration.FileState;
import axl.compiler.analysis.syntax.state.State;
import axl.compiler.analysis.syntax.state.expression.Expression;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

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

    public void test() {
        while (stream.hasNext()) {
            ExpressionState fileState = new ExpressionState(this, expression -> System.out.println(Main.formatString(expression)));

            states.push(fileState);
            while (!states.isEmpty()) {
                states.peek().analyze();
            }
        }
    }
}

