package calculator.controller;

import calculator.Calculator;
import calculator.Expression;
import calculator.NumberValue;
import calculator.parser.ExpressionParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController("legacyCalculatorController")
@RequestMapping("/api")
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            HttpServletRequest request) {
        try {
            String rawQuery = request.getQueryString();
            String expression = rawQuery.replace("expression=", "");
            // Decode all URL-encoded operators
            expression = expression.replace("%2B", "+");
            expression = expression.replace("%2F", "/");
            expression = expression.replace("%2A", "*");
            expression = expression.replace("%2D", "-");
            Expression expr = ExpressionParser.parse(expression);
            NumberValue result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of("result", result.toString()));
        } catch (ArithmeticException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Division by zero"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid expression: " + e.getMessage()));
        }
    }
}