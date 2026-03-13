package calculator;

import java.util.*;

public class RationalNumber {

    private final int numerator;
    private final int denominator;

    public RationalNumber(int numerator, int denominator) {
        if (denominator == 0) throw new ArithmeticException("Denominator cannot be zero");
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    public RationalNumber add(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    public RationalNumber subtract(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.denominator - other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(
                this.numerator * other.numerator,
                this.denominator * other.denominator
        );
    }

    public RationalNumber divide(RationalNumber other) {
        if (other.numerator == 0) throw new ArithmeticException("Division by zero");
        return new RationalNumber(
                this.numerator * other.denominator,
                this.denominator * other.numerator
        );
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

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
        return numerator + "/" + denominator;
    }

    public static RationalNumber compute(List<RationalNumber> numbers, List<String> operators) {
        if (numbers.size() != operators.size() + 1) {
            throw new IllegalArgumentException("Must have exactly one more number than operators");
        }

        // Priorité des opérateurs
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

            // Applique les opérateurs de priorité supérieure ou égale
            while (!opStack.isEmpty() && priority.get(opStack.peek()) >= priority.get(op)) {
                RationalNumber b = numStack.pop();
                RationalNumber a = numStack.pop();
                numStack.push(applyOp(a, opStack.pop(), b));
            }

            opStack.push(op);
            numStack.push(numbers.get(i + 1));
        }

        // Applique les opérateurs restants
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
}