package axl.compiler.analysis.syntax.ast;

import axl.compiler.analysis.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Type {

    private final List<Token> type;

    @Override
    public String toString() {
        return "Type {" +
                "location=" + type.stream().map(Token::toString).collect(Collectors.joining(".")) +
                "}";
    }

    // TODO
    // private final List<Type> generics;
}
