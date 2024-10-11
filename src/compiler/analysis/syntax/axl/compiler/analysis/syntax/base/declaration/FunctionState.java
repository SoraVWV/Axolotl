package axl.compiler.analysis.syntax.base.declaration;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.Argument;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.base.State;
import axl.compiler.analysis.syntax.base.StateController;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class FunctionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final Consumer<File.Function> result;

    public FunctionState(DefaultSyntaxAnalyzer analyzer, @NotNull Consumer<File.Function> result) {
        // TODO annotations
        this.analyzer = analyzer;
        this.result = result;
    }

    @Override
    public void analyze() {
        TokenStream stream = analyzer.getStream();
        stream.next(); // skip "fn"

        File.Function function = new File.Function();

        function.setName(analyzer.eat(TokenType.IDENTIFY));

        // TODO body without '{' '}'
        StateController.body(analyzer, function.getBody());

        // return type
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (analyzer.check(TokenType.IMPLICATION)) {
                analyzer.getStream().next();
                StateController.type(analyzer, function::setReturnType);
            }
        });

        // parse args
        argumentsState(function.getArguments());

        StateController.custom(analyzer, () -> {
            result.accept(function);
            analyzer.getStates().pop();
        });
    }

    private void argumentsState(List<Argument> arguments) {
        StateController.custom(analyzer, new State() {
            private boolean start = true;

            @Override
            public void analyze() {
                if (start) {
                    start = false;
                    analyzer.eat(TokenType.LEFT_PARENT);
                }

                Token name = analyzer.lowEat(TokenType.IDENTIFY);
                if (name != null) {
                    analyzer.eat(TokenType.COLON);
                    StateController.type(analyzer, type -> arguments.add(new Argument(type, name)));
                    return;
                }

                analyzer.eat(TokenType.RIGHT_PARENT);
                analyzer.getStates().pop();
            }
        });
    }
}
