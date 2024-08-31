package axl.compiler.analysis.lexical;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

@Getter
public enum TokenType {

    LEFT_PARENT(TokenGroup.DELIMITER, "("),
    RIGHT_PARENT(TokenGroup.DELIMITER, ")"),
    LEFT_BRACE(TokenGroup.DELIMITER, "{"),
    RIGHT_BRACE(TokenGroup.DELIMITER, "}"),
    LEFT_SQUARE(TokenGroup.DELIMITER, "["),
    RIGHT_SQUARE(TokenGroup.DELIMITER, "]"),
    COMMA(TokenGroup.DELIMITER, ","),
    DOT(TokenGroup.DELIMITER, "."),
    SEMI(TokenGroup.DELIMITER, ";"),
    TYPE(TokenGroup.DELIMITER, ":"),

    PLUS(TokenGroup.OPERATOR, "+"),
    MINUS(TokenGroup.OPERATOR, "-"),
    MULTIPLY(TokenGroup.OPERATOR, "*"),
    DIVIDE(TokenGroup.OPERATOR, "/"),
    MODULO(TokenGroup.OPERATOR, "%"),

    AND(TokenGroup.OPERATOR, "&&"),
    OR(TokenGroup.OPERATOR, "||"),
    NOT(TokenGroup.OPERATOR, "!"),

    EQUALS(TokenGroup.OPERATOR, "=="),
    NOT_EQUALS(TokenGroup.OPERATOR, "!="),
    GREATER(TokenGroup.OPERATOR, ">"),
    LESS(TokenGroup.OPERATOR, "<"),
    GREATER_OR_EQUAL(TokenGroup.OPERATOR, ">="),
    LESS_OR_EQUAL(TokenGroup.OPERATOR, "<="),

    ASSIGN(TokenGroup.OPERATOR, "="),
    PLUS_ASSIGN(TokenGroup.OPERATOR, "+="),
    MINUS_ASSIGN(TokenGroup.OPERATOR, "-="),
    MULTIPLY_ASSIGN(TokenGroup.OPERATOR, "*="),
    DIVIDE_ASSIGN(TokenGroup.OPERATOR, "/="),
    MODULO_ASSIGN(TokenGroup.OPERATOR, "%="),

    BIT_AND(TokenGroup.OPERATOR, "&"),
    BIT_OR(TokenGroup.OPERATOR, "|"),
    BIT_NOT(TokenGroup.OPERATOR, "~"),
    BIT_XOR(TokenGroup.OPERATOR, "^"),
    BIT_SHIFT_LEFT(TokenGroup.OPERATOR, "<<"),
    BIT_SHIFT_RIGHT(TokenGroup.OPERATOR, ">>"),

    BIT_AND_ASSIGN(TokenGroup.OPERATOR, "&="),
    BIT_OR_ASSIGN(TokenGroup.OPERATOR, "|="),
    BIT_XOR_ASSIGN(TokenGroup.OPERATOR, "^="),
    BIT_SHIFT_LEFT_ASSIGN(TokenGroup.OPERATOR, "<<="),
    BIT_SHIFT_RIGHT_ASSIGN(TokenGroup.OPERATOR, ">>="),

    UNARY_MINUS(TokenGroup.OPERATOR, "-"),
    INCREMENT(TokenGroup.OPERATOR, "++"),
    DECREMENT(TokenGroup.OPERATOR, "--"),

    AT_SYMBOL(TokenGroup.OPERATOR, "@"),
    QUESTION_MARK(TokenGroup.OPERATOR, "?"),

    PACKAGE(TokenGroup.KEYWORD, "package"),
    IMPORT(TokenGroup.KEYWORD, "import"),
    ENUM(TokenGroup.KEYWORD, "enum"),
    ANNOTATION(TokenGroup.KEYWORD, "annotation"),
    INTERFACE(TokenGroup.KEYWORD, "interface"),
    REF(TokenGroup.KEYWORD, "ref"),
    EXTENDS(TokenGroup.KEYWORD, "extends"),
    IMPLEMENTS(TokenGroup.KEYWORD, "implements"),

    FN(TokenGroup.KEYWORD, "fn"),
    RETURN(TokenGroup.KEYWORD, "return"),
    THIS(TokenGroup.KEYWORD, "this"),
    VAL(TokenGroup.KEYWORD, "val"),
    VAR(TokenGroup.KEYWORD, "var"),
    NEW(TokenGroup.KEYWORD, "new"),
    FREE(TokenGroup.KEYWORD, "free"),
    RAISE(TokenGroup.KEYWORD, "raise"),
    THROW(TokenGroup.KEYWORD, "throw"),
    TRY(TokenGroup.KEYWORD, "try"),
    CATCH(TokenGroup.KEYWORD, "catch"),
    FINALLY(TokenGroup.KEYWORD, "finally"),
    IF(TokenGroup.KEYWORD, "if"),
    ELSE(TokenGroup.KEYWORD, "else"),
    FOR(TokenGroup.KEYWORD, "for"),
    WHILE(TokenGroup.KEYWORD, "while"),
    SWITCH(TokenGroup.KEYWORD, "switch"),
    CASE(TokenGroup.KEYWORD, "case"),
    IS(TokenGroup.KEYWORD, "is"),

    IDENTIFY(TokenGroup.IDENTIFY),

    HEX_LONG_NUMBER(TokenGroup.LITERAL),
    BIN_LONG_NUMBER(TokenGroup.LITERAL),
    DEC_LONG_NUMBER(TokenGroup.LITERAL),
    HEX_NUMBER(TokenGroup.LITERAL),
    BIN_NUMBER(TokenGroup.LITERAL),
    DEC_NUMBER(TokenGroup.LITERAL),
    FLOAT_NUMBER(TokenGroup.LITERAL),
    DOUBLE_NUMBER(TokenGroup.LITERAL),
    FLOAT_EXP_NUMBER(TokenGroup.LITERAL),
    DOUBLE_EXP_NUMBER(TokenGroup.LITERAL),
    CHAR_LITERAL(TokenGroup.LITERAL),
    STRING_LITERAL(TokenGroup.LITERAL);

    private final TokenGroup group;

    private final String representation;

    TokenType(TokenGroup group, String representation) {
        this.group = group;
        this.representation = representation;
    }

    TokenType(TokenGroup group) {
        this.group = group;
        this.representation = null;
    }

    public static List<TokenType> delimitersAndOperators() {
        return Arrays.stream(TokenType.values())
                .filter(type -> type.group == TokenGroup.DELIMITER || type.group == TokenGroup.OPERATOR)
                .toList();
    }

    public static TokenType getByRepresentation(@NonNull String representation) {
        for (TokenType type: values())
            if (representation.equals(type.getRepresentation()))
                return type;

        return null;
    }
}
