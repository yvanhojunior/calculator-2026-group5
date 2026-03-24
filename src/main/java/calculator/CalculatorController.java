package calculator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST controller that exposes the calculator's arithmetic operations as HTTP endpoints.
 * All endpoints are available under the /api path.
 */
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    /**
     * Evaluates an arithmetic expression submitted via HTTP POST.
     *
     * <p>Expected JSON request body:
     * <pre>
     * {
     *   "operation": "plus" | "minus" | "times" | "divides",
     *   "numbers":   [&lt;integer&gt;, ...]
     * }
     * </pre>
     *
     * <p>Successful response body:
     * <pre>
     * {
     *   "result":     &lt;integer&gt;,
     *   "expression": "&lt;string representation of the expression&gt;"
     * }
     * </pre>
     *
     * @param body Map containing "operation" (String) and "numbers" (List of integers)
     * @return A JSON response with "result" and "expression", or an error message
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody Map<String, Object> body) {
        try {
            String operation = (String) body.get("operation");
            @SuppressWarnings("unchecked")
            List<Integer> rawNumbers = (List<Integer>) body.get("numbers");

            if (operation == null || rawNumbers == null || rawNumbers.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing or empty 'operation' or 'numbers' field."));
            }

            List<Expression> expressions = new ArrayList<>();
            for (int n : rawNumbers) {
                expressions.add(new MyNumber(n));
            }

            Expression expr = switch (operation.toLowerCase()) {
                case "plus"    -> new Plus(expressions);
                case "minus"   -> new Minus(expressions);
                case "times"   -> new Times(expressions);
                case "divides" -> new Divides(expressions);
                default -> throw new IllegalArgumentException("Unknown operation: " + operation);
            };

            int result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of(
                    "result",     result,
                    "expression", expr.toString()
            ));

        } catch (IllegalConstruction e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid expression: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Arithmetic error: " + e.getMessage()));
        }
    }
}