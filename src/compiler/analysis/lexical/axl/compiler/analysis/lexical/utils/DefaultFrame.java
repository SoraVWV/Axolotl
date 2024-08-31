package axl.compiler.analysis.lexical.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class DefaultFrame implements Frame {

    @Getter
    private int tokenId;
}
