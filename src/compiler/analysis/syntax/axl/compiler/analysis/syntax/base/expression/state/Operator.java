package axl.compiler.analysis.syntax.base.expression.state;

import axl.compiler.analysis.lexical.TokenType;
import lombok.Getter;

@Getter
enum Operator {
    RIGHT_PARENT(       TokenType.RIGHT_PARENT,         OperatorGenerator.PARENT,       0),
    LEFT_PARENT(        TokenType.LEFT_PARENT,          OperatorGenerator.EXCEPTION,    15),
    RIGHT_SQUARE(       TokenType.RIGHT_SQUARE,         OperatorGenerator.SQUARE,       0),
    LEFT_SQUARE(        TokenType.LEFT_SQUARE,          OperatorGenerator.EXCEPTION,    14),
    ASSIGN(             TokenType.ASSIGN,               OperatorGenerator.BINARY,       -1),
    PLUS_ASSIGN(        TokenType.PLUS_ASSIGN,          OperatorGenerator.BINARY,       -1),
    MINUS_ASSIGN(       TokenType.MINUS_ASSIGN,         OperatorGenerator.BINARY,       -1),
    MULTIPLY_ASSIGN(    TokenType.MULTIPLY_ASSIGN,      OperatorGenerator.BINARY,       -1),
    DIVIDE_ASSIGN(      TokenType.DIVIDE_ASSIGN,        OperatorGenerator.BINARY,       -1),
    MODULO_ASSIGN(      TokenType.MODULO_ASSIGN,        OperatorGenerator.BINARY,       -1),
    EQUALS(             TokenType.EQUALS,               OperatorGenerator.BINARY,       0),
    NOT_EQUALS(         TokenType.NOT_EQUALS,           OperatorGenerator.BINARY,       0),
    GREATER(            TokenType.GREATER,              OperatorGenerator.BINARY,       0),
    LESS(               TokenType.LESS,                 OperatorGenerator.BINARY,       0),
    GREATER_OR_EQUAL(   TokenType.GREATER_OR_EQUAL,     OperatorGenerator.BINARY,       0),
    LESS_OR_EQUAL(      TokenType.LESS_OR_EQUAL,        OperatorGenerator.BINARY,       0),
    ADD(                TokenType.PLUS,                 OperatorGenerator.BINARY,       1),
    SUB(                TokenType.MINUS,                OperatorGenerator.BINARY,       1),
    MULTI(              TokenType.MULTIPLY,             OperatorGenerator.BINARY,       2),
    DIV(                TokenType.DIVIDE,               OperatorGenerator.BINARY,       2),
    MOD(                TokenType.MODULO,               OperatorGenerator.BINARY,       2),
    BIT_AND(            TokenType.BIT_AND,              OperatorGenerator.BINARY,       3),
    BIT_OR(             TokenType.BIT_OR,               OperatorGenerator.BINARY,       4),
    AND(                TokenType.AND,                  OperatorGenerator.BINARY,       5),
    OR(                 TokenType.OR,                   OperatorGenerator.BINARY,       6),
    UNARY_MINUS(        TokenType.UNARY_MINUS,          OperatorGenerator.PREFIX,       7),
    NOT(                TokenType.NOT,                  OperatorGenerator.PREFIX,       7),
    BIT_NOT(            TokenType.BIT_NOT,              OperatorGenerator.PREFIX,       7),
    BIT_XOR(            TokenType.BIT_XOR,              OperatorGenerator.BINARY,       7),
    BIT_SHIFT_LEFT(     TokenType.BIT_SHIFT_LEFT,       OperatorGenerator.BINARY,       8),
    BIT_SHIFT_RIGHT(    TokenType.BIT_SHIFT_RIGHT,      OperatorGenerator.BINARY,       8),
    ACCESS(             TokenType.DOT,                  OperatorGenerator.BINARY,       9),
    IS(                 TokenType.IS,                   OperatorGenerator.BINARY,       9),
    AS(                 TokenType.AS,                   OperatorGenerator.BINARY,       9),
    INCREMENT(          TokenType.INCREMENT,            OperatorGenerator.UNARY,        9),
    DECREMENT(          TokenType.DECREMENT,            OperatorGenerator.UNARY,        9);

    private final TokenType operator;
    private final int priority;
    private final OperatorGenerator generator;

    Operator(TokenType operator, OperatorGenerator generator, int priority) {
        this.operator = operator;
        this.priority = priority;
        this.generator = generator;
    }
}