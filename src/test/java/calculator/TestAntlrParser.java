package calculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAntlrParser {

    @Test
    void shouldParseSimpleExpression() {
        String input = "2 + 3";
        calculator.ExpressionLexer lexer = new calculator.ExpressionLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        calculator.ExpressionParser parser = new calculator.ExpressionParser(tokens);
        calculator.ExpressionParser.ExpressionContext tree = parser.expression();
        assertNotNull(tree);
    }
}