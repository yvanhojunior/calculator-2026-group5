package calculator.controller;
import calculator.LinearEquationSolver;
import java.util.List;

import calculator.Calculator;
import calculator.Expression;
import calculator.parser.ExpressionParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController("queryCalculatorController")
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
            int result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of("result", result));
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

    @PostMapping("/solve")
    public ResponseEntity<Map<String, Object>> solve(@RequestBody Map<String, Object> body) {
        try {
            String equation = (String) body.get("equation");

            if (equation == null || equation.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing 'equation' field"));
            }

            LinearEquationSolver.Result result = LinearEquationSolver.solve(equation);

            return switch (result.getType()) {
                case UNIQUE -> ResponseEntity.ok(Map.of(
                        "type", "UNIQUE",
                        "solutions", result.getSolutions()
                ));
                case NO_SOLUTION -> ResponseEntity.ok(Map.of(
                        "type", "NO_SOLUTION",
                        "message", result.getErrorMessage()
                ));
                case INFINITE_SOLUTIONS -> ResponseEntity.ok(Map.of(
                        "type", "INFINITE_SOLUTIONS",
                        "message", result.getErrorMessage()
                ));
                case SYNTAX_ERROR -> ResponseEntity.badRequest().body(Map.of(
                        "type", "SYNTAX_ERROR",
                        "error", result.getErrorMessage()
                ));
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    @PostMapping("/solve/system")
    public ResponseEntity<Map<String, Object>> solveSystem(@RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            List<String> equations = (List<String>) body.get("equations");

            if (equations == null || equations.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing 'equations' field"));
            }

            LinearEquationSolver.Result result = LinearEquationSolver.solveSystem(equations);

            return switch (result.getType()) {
                case UNIQUE -> ResponseEntity.ok(Map.of(
                        "type", "UNIQUE",
                        "solutions", result.getSolutions()
                ));
                case NO_SOLUTION -> ResponseEntity.ok(Map.of(
                        "type", "NO_SOLUTION",
                        "message", result.getErrorMessage()
                ));
                case INFINITE_SOLUTIONS -> ResponseEntity.ok(Map.of(
                        "type", "INFINITE_SOLUTIONS",
                        "message", result.getErrorMessage()
                ));
                case SYNTAX_ERROR -> ResponseEntity.badRequest().body(Map.of(
                        "type", "SYNTAX_ERROR",
                        "error", result.getErrorMessage()
                ));
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}