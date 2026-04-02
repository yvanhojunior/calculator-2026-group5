package calculator;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LinearEquationSolver {

    public enum SolutionType {
        UNIQUE, NO_SOLUTION, INFINITE_SOLUTIONS, SYNTAX_ERROR
    }

    public static class Result {
        private final SolutionType type;
        private final Map<String, Double> solutions;
        private final String errorMessage;

        public Result(SolutionType type, Map<String, Double> solutions, String errorMessage) {
            this.type = type;
            this.solutions = solutions;
            this.errorMessage = errorMessage;
        }

        public SolutionType getType() { return type; }
        public Map<String, Double> getSolutions() { return solutions; }
        public String getErrorMessage() { return errorMessage; }

        @Override
        public String toString() {
            return switch (type) {
                case UNIQUE -> {
                    StringBuilder sb = new StringBuilder();
                    solutions.forEach((k, v) -> sb.append(k).append(" = ").append(v).append(", "));
                    yield sb.substring(0, sb.length() - 2);
                }
                case NO_SOLUTION -> errorMessage;
                case INFINITE_SOLUTIONS -> errorMessage;
                case SYNTAX_ERROR -> "Syntax error: " + errorMessage;
            };
        }
    }

    /**
     * Solves a single linear equation of the form ax + b = c
     * Example: "2x + 3 = 7" → x = 2.0
     */
    public static Result solve(String equation) {
        try {
            // Clean the equation
            equation = equation.trim().replaceAll("\\s+", " ");

            // Split the two sides
            String[] parts = equation.split("=");
            if (parts.length != 2) {
                return new Result(SolutionType.SYNTAX_ERROR, null, "Invalid equation format: missing '='");
            }

            String left = parts[0].trim();
            String right = parts[1].trim();

            // Validate right side is not empty
            if (right.isEmpty()) {
                return new Result(SolutionType.SYNTAX_ERROR, null, "Invalid equation format: missing right side");
            }

            // Parse the right side
            double rhs = Double.parseDouble(right);

            // Parse the left side: ax + b
            double a = 0, b = 0;

            // Look for the coefficient of x
            left = left.replace(" ", "");

            if (left.isEmpty()) {
                return new Result(SolutionType.SYNTAX_ERROR, null, "Invalid equation format: missing left side");
            }

            if (left.contains("x")) {
                String[] xParts = left.split("x");
                // Coefficient of x
                String coeff = xParts[0];
                if (coeff.isEmpty() || coeff.equals("+")) a = 1;
                else if (coeff.equals("-")) a = -1;
                else a = Double.parseDouble(coeff);

                // Constant
                if (xParts.length > 1 && !xParts[1].isEmpty()) {
                    b = Double.parseDouble(xParts[1]);
                }
            } else {
                b = Double.parseDouble(left);
            }

            // Resolution: ax = rhs - b
            double rhs2 = rhs - b;

            if (a == 0) {
                if (rhs2 == 0) {
                    return new Result(SolutionType.INFINITE_SOLUTIONS, null, "The equation has infinite solutions");
                } else {
                    return new Result(SolutionType.NO_SOLUTION, null, "The equation has no solution");
                }
            }

            double x = rhs2 / a;
            Map<String, Double> solutions = new HashMap<>();
            solutions.put("x", x);
            return new Result(SolutionType.UNIQUE, solutions, null);

        } catch (NumberFormatException e) {
            return new Result(SolutionType.SYNTAX_ERROR, null, "Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            return new Result(SolutionType.SYNTAX_ERROR, null, "Syntax error: " + e.getMessage());
        }
    }

    /**
     * Solves a system of two linear equations with two variables
     * Example: ["2x + y = 5", "x - y = 1"] → x = 2.0, y = 1.0
     */
    public static Result solveSystem(List<String> equations) {
        try {
            if (equations.size() != 2) {
                return new Result(SolutionType.SYNTAX_ERROR, null, "Only systems of 2 equations supported");
            }

            // Parse each equation in the form ax + by = c
            double[] eq1 = parseEquation(equations.get(0));
            double[] eq2 = parseEquation(equations.get(1));

            if (eq1 == null || eq2 == null) {
                return new Result(SolutionType.SYNTAX_ERROR, null, "Invalid equation format");
            }

            double a1 = eq1[0], b1 = eq1[1], c1 = eq1[2];
            double a2 = eq2[0], b2 = eq2[1], c2 = eq2[2];

            // Determinant
            double det = a1 * b2 - a2 * b1;

            if (det == 0) {
                if (a1 * c2 == a2 * c1) {
                    return new Result(SolutionType.INFINITE_SOLUTIONS, null, "The system has infinite solutions");
                } else {
                    return new Result(SolutionType.NO_SOLUTION, null, "The system has no solution");
                }
            }

            double x = (c1 * b2 - c2 * b1) / det;
            double y = (a1 * c2 - a2 * c1) / det;

            Map<String, Double> solutions = new HashMap<>();
            solutions.put("x", x);
            solutions.put("y", y);
            return new Result(SolutionType.UNIQUE, solutions, null);

        } catch (Exception e) {
            return new Result(SolutionType.SYNTAX_ERROR, null, "Syntax error: " + e.getMessage());
        }
    }

    /**
     * Parses an equation of the form ax + by = c
     * Returns [a, b, c] or null if error
     */
    private static double[] parseEquation(String equation) {
        try {
            equation = equation.trim().replaceAll("\\s+", "");
            String[] parts = equation.split("=");
            if (parts.length != 2) return null;

            String left = parts[0];
            double c = Double.parseDouble(parts[1]);
            double a = 0, b = 0;

            // Look for x
            if (left.contains("x")) {
                int xIdx = left.indexOf('x');
                String coeffX = left.substring(0, xIdx);
                if (coeffX.isEmpty() || coeffX.equals("+")) a = 1;
                else if (coeffX.equals("-")) a = -1;
                else a = Double.parseDouble(coeffX);
                left = left.substring(xIdx + 1);
            }

            // Look for y
            if (left.contains("y")) {
                int yIdx = left.indexOf('y');
                String coeffY = left.substring(0, yIdx);
                if (coeffY.isEmpty() || coeffY.equals("+")) b = 1;
                else if (coeffY.equals("-")) b = -1;
                else b = Double.parseDouble(coeffY);
            }

            return new double[]{a, b, c};
        } catch (Exception e) {
            return null;
        }
    }
}