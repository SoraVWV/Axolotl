package axl.compiler.analysis.lexical;

import axl.compiler.IFile;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface Tokenizer {

    @NonNull
    IFile getFile();

    @NonNull
    Token tokenize();

    boolean isProcessed();

}
