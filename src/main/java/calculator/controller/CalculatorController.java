package calculator.controller;

import calculator.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller for arithmetic expression evaluation.
 */
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    /**
     * Evaluates an arithmetic expression passed as a query parameter.
     * Example: GET /api/calculate?expression=3+4
     *
     * @param request the HTTP request containing the expression query string
     * @return the result of the evaluation or an error message
     */
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(HttpServletRequest request) {
        try {
            String rawQuery = request.getQueryString();
            String expression = rawQuery.replace("expression=", "");
            expression = expression.replace("%2B", "+");
            expression = expression.replace("%2F", "/");
            expression = expression.replace("%2A", "*");
            expression = expression.replace("%2D", "-");
            expression = expression.replace("%5E", "^");
            Expression expr = calculator.read(expression);
            NumberValue result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of("result", result.toString()));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Division by zero"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid expression: " + e.getMessage()));
        }
    }
}