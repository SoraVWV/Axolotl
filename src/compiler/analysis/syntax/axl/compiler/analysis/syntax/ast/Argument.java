package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.SyntaxAnalyzer;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.SubAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Argument extends Node {

    private List<Annotation> annotations;

    private Token name;

    private Type type;

    @SubAnalyzer(target = Argument.class)
    public static class ArgumentAnalyzer extends Analyzer {

        @Override
        @SuppressWarnings("all")
        public Node analyze(SyntaxAnalyzer syntaxAnalyzer, TokenStream tokenStream) {
            List<Annotation> annotations = (List<Annotation>) syntaxAnalyzer.analyze(tokenStream, (TokenType) null, Annotation.class);
            Token name = tokenStream.next();
            if (name == null || name.getType() != TokenType.IDENTIFY)
                return null;

            Token op = tokenStream.next();
            if (op == null || op.getType() != TokenType.TYPE)
                return null;

            Type type = (Type) syntaxAnalyzer.analyze(tokenStream, Type.class);
            if (type == null)
                return null;

            return new Argument(annotations, name, type);
        }
    }

}
