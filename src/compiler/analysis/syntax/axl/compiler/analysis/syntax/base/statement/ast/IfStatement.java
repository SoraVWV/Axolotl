package axl.compiler.analysis.syntax.base.statement.ast;

import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.base.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class IfStatement {

    private final Expression expression;

    private final List<Node> body;

    private final List<Node> elseBody;

    @Override
    public String toString() {
        return "If {" +
                "expression=" + expression +
                "}";
    }

    // TODO
    // private final List<Type> generics;
}
