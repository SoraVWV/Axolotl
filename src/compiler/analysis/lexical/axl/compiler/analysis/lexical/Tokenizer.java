package axl.compiler.analysis.lexical;

import axl.compiler.IFile;
import lombok.NonNull;

public interface Tokenizer {

    @NonNull IFile getFile();

    @NonNull Token tokenize();

    boolean isProcessed();
}
