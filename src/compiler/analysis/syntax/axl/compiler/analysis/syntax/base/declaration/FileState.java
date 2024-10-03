package axl.compiler.analysis.syntax.base.declaration;

import axl.compiler.analysis.lexical.Token;
import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.lexical.utils.Frame;
import axl.compiler.analysis.lexical.utils.TokenStream;
import axl.compiler.analysis.syntax.DefaultSyntaxAnalyzer;
import axl.compiler.analysis.syntax.ast.File;
import axl.compiler.analysis.syntax.base.State;
import axl.compiler.analysis.syntax.utils.IllegalSyntaxException;
import axl.compiler.analysis.syntax.utils.StateUtils;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class FileState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final TokenStream stream;

    private final File file;

    public FileState(DefaultSyntaxAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.stream = analyzer.getStream();

        this.file = new File();
        this.file.setFile(stream.getFile());
    }

    @SneakyThrows
    @Override
    public void analyze() {
        if (file.getLocation() == null)
            setup();

        if (stream.hasNext())
            throw new IllegalSyntaxException("Undefined token", stream);
        // TODO functions, classes, structs, events, listeners
    }

    private void setup() {
        this.analyzer.getNodes().push(this.file);
        if (stream.get().getType() == TokenType.PACKAGE) {
            Frame frame = stream.createFrame();
            stream.next();
            file.setLocation(StateUtils.getLocation(analyzer));
            if (stream.hasNext() && stream.get().getType() == TokenType.DOT) {
                stream.restoreFrame(frame);
                throw new IllegalSyntaxException("Invalid location", stream);
            }
        } else {
            file.setLocation(new ArrayList<>());
        }

        while (stream.hasNext() && stream.get().getType() == TokenType.IMPORT) {
            stream.next();
            List<Token> location = StateUtils.getLocation(analyzer);
            boolean all = false;

            if (stream.hasNext() && stream.get().getType() == TokenType.DOT) {
                stream.next();

                if (stream.hasNext() && stream.get().getType() != TokenType.MULTIPLY)
                    throw new IllegalSyntaxException("Invalid location", stream);

                stream.next();
                all = true;
            }

            this.file.getImports().add(new File.Import(location, all));
        }
    }

    public File build() {
        return this.file;
    }
}
