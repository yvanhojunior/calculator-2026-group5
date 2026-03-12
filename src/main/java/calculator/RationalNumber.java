package calculator;

import java.util.Objects;

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
}