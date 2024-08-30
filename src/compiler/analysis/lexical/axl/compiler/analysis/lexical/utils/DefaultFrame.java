package axl.compiler.analysis.lexical.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class DefaultFrame implements Frame {

    @Getter
    @NonNull
    private int tokenId;
}
