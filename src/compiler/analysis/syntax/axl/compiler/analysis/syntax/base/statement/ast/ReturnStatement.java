package axl.compiler.analysis.syntax.base.statement.ast;

import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.base.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnStatement implements Node {

    private final Expression expression;

    @Override
    public String toString() {
        return "Return {" +
                "expression=" + expression +
                "}";
    }
}
