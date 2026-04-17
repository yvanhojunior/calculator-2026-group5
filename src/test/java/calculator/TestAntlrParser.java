package calculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAntlrParser {

    @Test
    void shouldBuildAst() {
        calculator.ExpressionLexer lexer = new calculator.ExpressionLexer(CharStreams.fromString("2 + 3"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        calculator.ExpressionParser parser = new calculator.ExpressionParser(tokens);

        calculator.ExpressionParser.ExpressionContext tree = parser.expression();

        ExpressionAstBuilder builder = new ExpressionAstBuilder();
        Expression expr = builder.visit(tree);

        assertNotNull(expr);
    }

    /**
     * this test valids complet integration of parser ANTLR in the business engine
     * Goal: 1. check that the chain is correctly parsed in AST
     *       2. check that evaluation respect the priority of operators
     *       3. if the parser is incorrect, this test will fail
     */
    @Test
    void shouldComputeExpressionCorrectly() {
        Calculator calc = new Calculator();

        Expression expr = calc.read("2 + 3 * 4");
        NumberValue result = calc.eval(expr);

        assertEquals(new IntegerValue(14), result);
    }

    /**
     * this test valid that parser manager correctly the parentheses.
     * here, the priority is inversed: (2 + 3) * 4 = 20 instead of 14
     * if this test fails, it means the syntax tree is incorrectly built
     */
    @Test
    void shouldRespectParenthesesPriority() {

        Calculator calc = new Calculator();

        Expression expr = calc.read("(2 + 3) * 4");
        NumberValue result = calc.eval(expr);

        assertEquals(new IntegerValue(20), result);
    }

    @Test
    /**
     * This test valid that the parser correctly built an long expression with multiple operators and priority levels.
     * We check here that evaluation is done from left to right.
     * while respecting the priorities (* and / before + and -)
     */
    void shouldHandleChainedOperations() {

        Calculator calc = new Calculator();

        Expression expr = calc.read("10 - 2 * 3 + 4");
        NumberValue result = calc.eval(expr);

        assertEquals(new IntegerValue(8), result);
    }

    @Test
    void shouldComputeModuloAsRemainder() {
        Calculator calc = new Calculator();

        Expression expr = calc.read("9 % 8");
        NumberValue result = calc.eval(expr);

        assertEquals(new IntegerValue(1), result);
    }


}