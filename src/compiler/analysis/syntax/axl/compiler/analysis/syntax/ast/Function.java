package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.expression.Expression;
import axl.compiler.analysis.syntax.ast.statement.ReturnStatement;
import axl.compiler.analysis.syntax.ast.statement.Statement;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Function extends Node {

    private List<Annotation> annotations;

    private Token name;

    private List<Argument> arguments;

    private Type returnType;

    private Statement body;

    @SubAnalyzer(target = Function.class)
    public static class FunctionAnalyzer extends Analyzer {

        @Override
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            List<Annotation> annotations = (List<Annotation>) syntaxAnalyzer.analyze(tokenStream, Annotation.class);

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() != TokenType.FN)
                return null;

            Token name = tokenStream.next();
            if (name == null || name.getType() != TokenType.IDENTIFY)
                return null;

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() != TokenType.LEFT_PARENT)
                return null;

            List<Argument> arguments = (List<Argument>) syntaxAnalyzer.analyze(tokenStream, TokenType.COMMA, Argument.class);

            if (!tokenStream.hasNext())
                return null;

            if (tokenStream.next().getType() != TokenType.RIGHT_PARENT)
                return null;

            if (!tokenStream.hasNext())
                return null;

            Type type = null;
            if (tokenStream.get().getType() == TokenType.GREATER) {
                tokenStream.next();
                type = (Type) syntaxAnalyzer.analyze(tokenStream, Type.class);
            }

            Node body = syntaxAnalyzer.analyze(tokenStream, Statement.class, Expression.class);
            if (body == null)
                return null;

            if (body instanceof Expression)
                body = new ReturnStatement((Expression) body);

            return new Function(annotations, name, arguments, type, (Statement) body);
        }
    }

}
