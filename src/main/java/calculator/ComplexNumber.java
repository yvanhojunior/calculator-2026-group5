package calculator;

import java.util.Objects;

public class ComplexNumber implements NumberType<ComplexNumber> {

    private final double real;
    private final double imaginary;
    private final boolean error;
    private final String errorMessage;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        this.error = false;
        this.errorMessage = null;
    }

    // Constructeur pour les cas d'erreur
    private ComplexNumber(String errorMessage) {
        this.real = 0;
        this.imaginary = 0;
        this.error = true;
        this.errorMessage = errorMessage;
    }

    public double getReal() { return real; }
    public double getImaginary() { return imaginary; }

    @Override
    public boolean isError() { return error; }

    @Override
    public String getErrorMessage() { return errorMessage; }

    @Override
    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(
                this.real + other.real,
                this.imaginary + other.imaginary
        );
    }

    @Override
    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(
                this.real - other.real,
                this.imaginary - other.imaginary
        );
    }

    @Override
    public ComplexNumber multiply(ComplexNumber other) {
        return new ComplexNumber(
                this.real * other.real - this.imaginary * other.imaginary,
                this.real * other.imaginary + this.imaginary * other.real
        );
    }

    @Override
    public ComplexNumber divide(ComplexNumber other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        return new ComplexNumber(
                (this.real * other.real + this.imaginary * other.imaginary) / denominator,
                (this.imaginary * other.real - this.real * other.imaginary) / denominator
        );
    }

    // Racine carrée — retourne NaN si négatif pur (pas de partie imaginaire)
    public ComplexNumber sqrt() {
        if (imaginary == 0 && real < 0) {
            return new ComplexNumber(0, Math.sqrt(-real));
        }
        double r = Math.sqrt(real * real + imaginary * imaginary);
        double re = Math.sqrt((r + real) / 2);
        double im = Math.signum(imaginary) * Math.sqrt((r - real) / 2);
        return new ComplexNumber(re, im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexNumber)) return false;
        ComplexNumber other = (ComplexNumber) o;
        return Double.compare(real, other.real) == 0 &&
                Double.compare(imaginary, other.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public String toString() {
        if (error) return "Error: " + errorMessage;
        if (imaginary >= 0) return real + "+" + imaginary + "i";
        return real + "" + imaginary + "i";
    }
}