package axl.compiler.analysis.syntax.state.expression.state;

import axl.compiler.IFile;
import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenGroup;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.Frame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.state.Node;
import axl.compiler.analysis.syntax.state.State;
import axl.compiler.analysis.syntax.state.expression.*;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

@Getter
public class ExpressionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    @Nullable
    private final Consumer<Expression> result;

    @Setter
    private Boolean lastExpression = false;

    private Boolean nextState = false;

    private final int levelNodes;

    private final int levelContext;

    int parents = 0;

    int square = 0;

    public ExpressionState(DefaultSyntaxAnalyzer analyzer, @Nullable Consumer<Expression> result) {
        this.analyzer = analyzer;
        this.levelNodes = analyzer.getNodes().size();
        this.levelContext = analyzer.getContext().size();
        this.result = result;
    }

    @SneakyThrows
    @Override
    public void analyze() {
        final TokenStream stream = analyzer.getStream();

        main:
        while (stream.hasNext())  {
            if (stream.get().getType().getGroup() == TokenGroup.OPERATOR || stream.get().getType().getGroup() == TokenGroup.DELIMITER) {
                Frame frame = stream.createFrame();
                switch (stream.get().getType()) {
                    case SEMI:
                        stream.next();
                    case COMMA:
                        break main;
                }
                OperatorEntry entry = findOperator(stream.getFile(), stream.next());

                if (entry.getOperator().getOperator() == TokenType.RIGHT_PARENT) {
                    if (parents == 0) {
                        stream.restoreFrame(frame);
                        break;
                    }

                    entry.accept(this);
                    continue;
                } else if (entry.getOperator().getOperator() == TokenType.LEFT_PARENT) {
                    parents++;
                } else if (entry.getOperator().getOperator() == TokenType.RIGHT_SQUARE) {
                    if (square == 0) {
                        stream.restoreFrame(frame);
                        break;
                    }

                    entry.accept(this);
                    continue;
                } else if (entry.getOperator().getOperator() == TokenType.LEFT_SQUARE) {
                    if (!lastExpression)
                        throw new RuntimeException(); // TODO output

                    square++;
                }

                reduce(entry.getOperator().getPriority());
                lastExpression = false;
                if (entry.getType() == OperatorEntry.OperatorType.POSTFIX) {
                    if (stream.peekLastLine(frame) == entry.getToken().getLine()) {
                        lastExpression = true;
                    } else {
                        stream.restoreFrame(frame);
                        break;
                    }
                }
                pushContext(entry);
            } else if (findPrimary(stream)) {
                lastExpression = true;
            } else {
                nextState = true;
                break;
            }
        }

        if (nextState) {
            nextState = false;

            if (analyzer.getStates().peek() != this)
                return;
        }

        reduce();
        if (sizeExpressions() != 1) {
            throw new RuntimeException();
        }

        analyzer.getStates().pop();
        if (result != null)
            result.accept(popExpression());
    }

    private boolean findPrimary(TokenStream stream) {
        if (getLastExpression()) {
            if (parents == 0 && square == 0)
                return false;

            throw new RuntimeException(); // TODO output
        }

        if (stream.get().getType().getGroup() == TokenGroup.LITERAL) {
            pushExpression(new LiteralExpression(stream.next()));
        } else if (stream.get().getType() == TokenType.IDENTIFY) {
            Token name = stream.next();
            Frame frame = stream.createFrame();

            if (!stream.hasNext() || stream.get().getType() != TokenType.LEFT_PARENT) {
                stream.restoreFrame(frame);
                pushExpression(new IdentifyExpression(name));
                return true;
            }

            stream.next();

            MethodExpression methodExpression = new MethodExpression(name, new ArrayList<>());
            analyzer.getStates().push(new MethodExpressionState(analyzer, methodExpression, (expression) -> {
                pushExpression(expression);
                lastExpression = true;
            }));
            return false;
        } else if (stream.get().getType() == TokenType.VAR) {
            if (!stream.hasNext())
                throw new RuntimeException();

            stream.next();
            if (!stream.hasNext())
                throw new RuntimeException();

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new RuntimeException();

            pushExpression(new VariableDefineExpression(null, token));
        } else if (stream.get().getType() == TokenType.VAL) {
            if (!stream.hasNext())
                throw new RuntimeException();

            stream.next();
            if (!stream.hasNext())
                throw new RuntimeException();

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new RuntimeException();

            pushExpression(new ValueDefineExpression(null, token));
        } else {
            return false;
        }

        return true;
    }

    private OperatorEntry findOperator(IFile file, Token token) {
        for (Operator operator : Operator.values()) {
            if (operator.getOperator() == token.getType()) {
                if (operator.getGenerator() == OperatorGenerator.UNARY) {
                    if (lastExpression)
                        return new OperatorEntry(operator, token, OperatorEntry.OperatorType.POSTFIX);
                    else
                        return new OperatorEntry(operator, token, OperatorEntry.OperatorType.PREFIX);
                }
                if (operator.getGenerator() == OperatorGenerator.PREFIX)
                    return new OperatorEntry(operator, token, OperatorEntry.OperatorType.PREFIX);

                return new OperatorEntry(operator, token, OperatorEntry.OperatorType.BINARY);
            }
        }

        throw new IllegalSyntaxException("Unknown operator: " + token.getType(), file, token);
    }

    private void reduce(int priority) {
        while (sizeContext() != 0 &&
                priority <= peekContext().getOperator().getPriority() &&
                peekContext().getOperator().getOperator() != TokenType.LEFT_PARENT &&
                peekContext().getOperator().getOperator() != TokenType.LEFT_SQUARE) {
            peekContext().accept(this);
        }
    }

    private void reduce() {
        while (sizeContext() != 0)
            peekContext().accept(this);
    }

    int sizeExpressions() {
        return analyzer.getNodes().size() - levelNodes;
    }

    void pushExpression(Expression expression) {
        this.analyzer.getNodes().push(expression);
    }

    Expression popExpression() {
        if (sizeExpressions() <= 0)
            throw new IllegalStateException("Не должно быть вызвано");

        Node node = this.analyzer.getNodes().pop();

        if (node instanceof Expression)
            return (Expression) node;

        throw new IllegalStateException("Не должно быть вызвано");
    }

    Expression peekExpression() {
        if (sizeExpressions() <= 0)
            throw new IllegalStateException("Не должно быть вызвано");

        Node node = this.analyzer.getNodes().peek();

        if (node instanceof Expression)
            return (Expression) node;

        throw new IllegalStateException("Не должно быть вызвано");
    }

    int sizeContext() {
        return analyzer.getContext().size() - levelContext;
    }

    void pushContext(OperatorEntry entry) {
        this.analyzer.getContext().push(entry);
    }

    OperatorEntry popContext() {
        if (sizeContext() <= 0)
            throw new IllegalStateException("Не должно быть вызвано");

        Object object = this.analyzer.getContext().pop();

        if (object instanceof OperatorEntry)
            return (OperatorEntry) object;

        throw new IllegalStateException("Не должно быть вызвано");
    }

    OperatorEntry peekContext() {
        if (sizeContext() <= 0)
            throw new IllegalStateException("Не должно быть вызвано");

        Object object = this.analyzer.getContext().peek();

        if (object instanceof OperatorEntry)
            return (OperatorEntry) object;

        throw new IllegalStateException("Не должно быть вызвано");
    }
}
