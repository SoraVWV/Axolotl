package axl.compiler.analysis.syntax;

import axl.compiler.Main;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.state.ExpressionState;
import axl.compiler.analysis.syntax.state.FileState;
import axl.compiler.analysis.syntax.state.State;
import axl.compiler.analysis.syntax.state.expression.Expression;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.Stack;

@Getter
@Setter
public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer<File> {

    private final Stack<State> states = new Stack<>();

    private final Stack<Expression> expressions = new Stack<>();

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

    public Expression test() {
        ExpressionState fileState = new ExpressionState(this);
        states.push(fileState);

        while(!states.isEmpty()) {
            states.peek().analyze();
        }

        if (stream.hasNext())
            throw new RuntimeException();

        return expressions.pop();
        //System.out.println(Main.formatString(expressions.pop()));
    }
}

