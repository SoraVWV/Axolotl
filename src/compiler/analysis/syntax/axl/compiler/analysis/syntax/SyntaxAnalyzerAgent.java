package axl.compiler.analysis.syntax;

import axl.compiler.analysis.lexical.TokenType;
import axl.compiler.analysis.syntax.ast.Argument;
import axl.compiler.analysis.syntax.ast.Function;
import axl.compiler.analysis.syntax.ast.expression.*;
import axl.compiler.analysis.syntax.ast.expression.NumberExpression;
import axl.compiler.analysis.syntax.ast.statement.Block;
import axl.compiler.analysis.syntax.ast.statement.ConditionStatement;
import axl.compiler.analysis.syntax.ast.statement.ReturnStatement;

import java.util.List;

public class SyntaxAnalyzerAgent {

    public static SyntaxAnalyzer createSyntaxAnalyzer() {
        SyntaxAnalyzer syntaxAnalyzer = new DefaultSyntaxAnalyzer();

        syntaxAnalyzer.addAnalyzer(new ConditionStatement.ConditionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new ReturnStatement.ReturnAnalyzer());
        syntaxAnalyzer.addAnalyzer(new Argument.ArgumentAnalyzer());
        syntaxAnalyzer.addAnalyzer(new Function.FunctionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new Block.BlockAnalyzer());

        // низкий приоритет VVV
        syntaxAnalyzer.addAnalyzer(new VariableDefine.VariableDefineAnalyzer());
        syntaxAnalyzer.addAnalyzer(new AssigmentExpression.AssigmentExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.OR)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.AND)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.BIT_OR)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.BIT_XOR)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.BIT_AND)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.EQUALS, TokenType.NOT_EQUALS)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.LESS, TokenType.LESS_OR_EQUAL, TokenType.GREATER, TokenType.GREATER_OR_EQUAL)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.BIT_SHIFT_LEFT, TokenType.BIT_SHIFT_RIGHT)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.PLUS, TokenType.MINUS)));
        syntaxAnalyzer.addAnalyzer(new BinaryExpression.BinaryExpressionAnalyzer(List.of(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)));
        syntaxAnalyzer.addAnalyzer(new UnaryExpression.UnaryExpressionPrefixAnalyzer());
        syntaxAnalyzer.addAnalyzer(new UnaryExpression.UnaryExpressionIncrementDecrementAnalyzer());
        syntaxAnalyzer.addAnalyzer(new CallFunctionExpression.CallFunctionExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new ArrayAccessExpression.ArrayAccessExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new InstanceAccessExpression.InstanceAccessExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new BracketsExpression.BracketsExpressionAnalyzer());
        // высокий приоритет ^^^

        // так надо VVV
        syntaxAnalyzer.addAnalyzer(new NumberExpression.NumberExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new CharExpression.CharExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new StringExpression.StringExpressionAnalyzer());
        syntaxAnalyzer.addAnalyzer(new VariableUseExpression.VariableUseAnalyzer());

        return syntaxAnalyzer;
    }

}
