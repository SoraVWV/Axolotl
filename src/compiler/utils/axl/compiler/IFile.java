package axl.compiler;

import axl.compiler.analysis.lexical.utils.TokenStream;

public interface IFile {

    String getFilename();

    String getContent();

    TokenStream createTokenStream();
}

