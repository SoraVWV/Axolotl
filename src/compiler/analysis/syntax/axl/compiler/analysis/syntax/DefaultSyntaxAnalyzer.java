package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.ast.Node;
import axl.compiler.analysis.syntax.utils.Analyzer;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import lombok.NonNull;

import java.util.List;
import java.util.Stack;

public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer {

    private final Stack<Node> stack = new Stack<>();

    @Override
    public @NonNull File analyze(TokenStream tokenStream) {
        File file = new File();
        file.setFile(tokenStream.getFile());

        while (tokenStream.hasNext() && tokenStream.get().getType() == TokenType.IMPORT) {
            tokenStream.next();
            if (!tokenStream.hasNext())
                throw new IllegalSyntaxException("Corrupted file. Failed to read 'import'", tokenStream);

            while (tokenStream.hasNext())

            File.Import fileImport = new File.Import();
        }
    }

    private List<File.Import> analyzeImports() {

    }

}

