package calculator.controller;

import calculator.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

/**
 * REST controller for arithmetic expression evaluation.
 */
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    private Map<String, Object> errorResponse(String errorCode, String errorMessage) {
        return Map.of(
                "errorCode", errorCode,
                "error", errorMessage
        );
    }

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
            String expression = request.getParameter("expression");
            if (expression == null || expression.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(errorResponse("errors.missing_expression_parameter", "Missing expression parameter"));
            }
            Expression expr = calculator.read(expression);
            NumberValue result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of("result", result.toString()));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().body(errorResponse("errors.division_by_zero", "Division by zero"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse("errors.invalid_expression", "Invalid expression: " + e.getMessage()));
        }
    }

    @PostMapping("/switchFormat")
    public ResponseEntity<Map<String, Object>> switchFormat(@RequestBody Map<String, String> request) {
        String numerator = request.get("numerator");
        String denominator = request.get("denominator");
        if (numerator == null || denominator == null) {
            return ResponseEntity.badRequest().body(errorResponse("errors.missing_numerator_or_denominator", "Missing numerator or denominator"));
        }
        try {
            RealValue decimalValue = new RationalValue(Long.parseLong(numerator), Long.parseLong(denominator)).toReal();
            return ResponseEntity.ok(Map.of("decimalValue", decimalValue.toString()));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(errorResponse("errors.invalid_number_format", "Invalid number format"));
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "calculator-api",
                "timestamp", Instant.now().toString(),
                "path", request.getRequestURI()
        ));
    }
}