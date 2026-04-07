package calculator.controller;

import calculator.LinearEquationSolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for linear equation solving.
 */
@RestController
@RequestMapping("/api")
public class SolveController {

    /**
     * Solves a single linear equation.
     * Example body: {"equation": "2x + 3 = 7"}
     *
     * @param body the request body containing the equation
     * @return the solution or an error message
     */
    @PostMapping("/solve")
    public ResponseEntity<Map<String, Object>> solve(@RequestBody Map<String, Object> body) {
        try {
            String equation = (String) body.get("equation");

            if (equation == null || equation.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing 'equation' field"));
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
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    /**
     * Solves a system of linear equations.
     * Example body: {"equations": ["x + y = 3", "x - y = 1"]}
     *
     * @param body the request body containing the list of equations
     * @return the solutions or an error message
     */
    @PostMapping("/solve/system")
    public ResponseEntity<Map<String, Object>> solveSystem(@RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            List<String> equations = (List<String>) body.get("equations");

            if (equations == null || equations.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing 'equations' field"));
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
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}