package calculator.parser;

import calculator.*;
import java.util.Arrays;

public class ExpressionParser {
    private ExpressionParser() {
        /* This utility class should not be instantiated */
    }


    public static Expression parse(String expression) throws Exception {
        expression = expression.trim();

        if (expression.isEmpty()) {
            throw new Exception("Empty expression");
        }

        int pos = findMainOperator(expression);

        if (pos == -1) {
            // Pas d'opérateur = c'est un nombre
            try {
                if (expression.contains(".")) {
                    return new MyNumber(new RealValue(Double.parseDouble(expression)));
                } else {
                    return new MyNumber(Integer.parseInt(expression));
                }
            } catch (NumberFormatException e) {
                throw new Exception("Invalid number: " + expression);
            }
        }

        char op = expression.charAt(pos);
        String left = expression.substring(0, pos).trim();
        String right = expression.substring(pos + 1).trim();

        Expression leftExpr = parse(left);
        Expression rightExpr = parse(right);

        return switch (op) {
            case '+' -> new Plus(Arrays.asList(leftExpr, rightExpr));
            case '-' -> new Minus(Arrays.asList(leftExpr, rightExpr));
            case '*' -> new Times(Arrays.asList(leftExpr, rightExpr));
            case '/' -> new Divides(Arrays.asList(leftExpr, rightExpr));
            default -> throw new Exception("Unknown operator: " + op);
        };
    }

    private static int findMainOperator(String expr) {
        int depth = 0;
        int pos = -1;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && (c == '+' || c == '-')) pos = i;
            else if (depth == 0 && (c == '*' || c == '/') && pos == -1) pos = i;
        }
        return pos;
    }
}