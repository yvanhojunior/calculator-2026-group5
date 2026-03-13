package calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class RationalNumber implements NumberType<RationalNumber> {

    private final int numerator;
    private final int denominator;
    private final boolean error;
    private final String errorMessage;

    public RationalNumber(int numerator, int denominator) {
        if (denominator == 0) {
            this.numerator = 0;
            this.denominator = 1;
            this.error = true;
            this.errorMessage = "Division by zero";
        } else {
            int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
            this.error = false;
            this.errorMessage = null;
        }
    }

    @Override
    public boolean isError() { return error; }

    @Override
    public String getErrorMessage() { return errorMessage; }

    @Override
    public RationalNumber add(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    @Override
    public RationalNumber subtract(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.denominator - other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    @Override
    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.numerator,
                this.denominator * other.denominator
        );
    }

    @Override
    public RationalNumber divide(RationalNumber other) {
        if (other.numerator == 0) throw new ArithmeticException("Division by zero");
        return new RationalNumber(
                this.numerator * other.denominator,
                this.denominator * other.numerator
        );
    }

    // Méthodes récursives pour n opérandes
    public static RationalNumber add(List<RationalNumber> numbers) {
        if (numbers.size() == 1) return numbers.get(0);
        return numbers.get(0).add(add(numbers.subList(1, numbers.size())));
    }

    public static RationalNumber subtract(List<RationalNumber> numbers) {
        if (numbers.size() == 1) return numbers.get(0);
        return numbers.get(0).subtract(subtract(numbers.subList(1, numbers.size())));
    }

    public static RationalNumber multiply(List<RationalNumber> numbers) {
        if (numbers.size() == 1) return numbers.get(0);
        return numbers.get(0).multiply(multiply(numbers.subList(1, numbers.size())));
    }

    public static RationalNumber divide(List<RationalNumber> numbers) {
        if (numbers.size() == 1) return numbers.get(0);
        return numbers.get(0).divide(divide(numbers.subList(1, numbers.size())));
    }

    // Méthode compute avec priorité des opérateurs (Shunting Yard)
    public static RationalNumber compute(List<RationalNumber> numbers, List<String> operators) {
        if (numbers.size() != operators.size() + 1) {
            throw new IllegalArgumentException("Must have exactly one more number than operators");
        }

        Map<String, Integer> priority = new HashMap<>();
        priority.put("+", 1);
        priority.put("-", 1);
        priority.put("*", 2);
        priority.put("/", 2);

        Stack<RationalNumber> numStack = new Stack<>();
        Stack<String> opStack = new Stack<>();

        numStack.push(numbers.get(0));

        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            while (!opStack.isEmpty() && priority.get(opStack.peek()) >= priority.get(op)) {
                RationalNumber b = numStack.pop();
                RationalNumber a = numStack.pop();
                numStack.push(applyOp(a, opStack.pop(), b));
            }
            opStack.push(op);
            numStack.push(numbers.get(i + 1));
        }

        while (!opStack.isEmpty()) {
            RationalNumber b = numStack.pop();
            RationalNumber a = numStack.pop();
            numStack.push(applyOp(a, opStack.pop(), b));
        }

        return numStack.pop();
    }

    private static RationalNumber applyOp(RationalNumber a, String op, RationalNumber b) {
        return switch (op) {
            case "+" -> a.add(b);
            case "-" -> a.subtract(b);
            case "*" -> a.multiply(b);
            case "/" -> a.divide(b);
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public int getNumerator() { return numerator; }
    public int getDenominator() { return denominator; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RationalNumber)) return false;
        RationalNumber other = (RationalNumber) o;
        return numerator == other.numerator && denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public String toString() {
        if (error) return "Error: " + errorMessage;
        return numerator + "/" + denominator;
    }
}