package calculator;

import java.util.Objects;

public class RealNumber implements NumberType<RealNumber> {

    private final double value;
    private final int precision;

    public RealNumber(double value, int precision) {
        this.value = value;
        this.precision = precision;
    }

    public RealNumber(double value) {
        this(value, 10);
    }

    public double getValue() { return value; }
    public int getPrecision() { return precision; }

    @Override
    public RealNumber add(RealNumber other) {
        return new RealNumber(this.value + other.value, precision);
    }

    @Override
    public RealNumber subtract(RealNumber other) {
        return new RealNumber(this.value - other.value, precision);
    }

    @Override
    public RealNumber multiply(RealNumber other) {
        return new RealNumber(this.value * other.value, precision);
    }

    @Override
    public RealNumber divide(RealNumber other) {
        // Pas d'exception — retourne NaN ou Infinity comme Java le fait nativement
        return new RealNumber(this.value / other.value, precision);
    }

    public RealNumber sqrt() {
        if (value < 0) return new RealNumber(Double.NaN, precision);
        return new RealNumber(Math.sqrt(value), precision);
    }

    public RealNumber log() {
        if (value < 0) return new RealNumber(Double.NaN, precision);
        return new RealNumber(Math.log(value), precision);
    }

    @Override
    public boolean isError() {
        return Double.isNaN(value) || Double.isInfinite(value);
    }

    @Override
    public String getErrorMessage() {
        if (Double.isNaN(value)) return "NaN";
        if (Double.isInfinite(value)) return value > 0 ? "+Infinity" : "-Infinity";
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RealNumber)) return false;
        RealNumber other = (RealNumber) o;
        if (Double.isNaN(this.value) && Double.isNaN(other.value)) return true;
        double epsilon = Math.pow(10, -precision);
        return Math.abs(this.value - other.value) < epsilon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (Double.isNaN(value)) return "NaN";
        if (Double.isInfinite(value)) return value > 0 ? "Infinity" : "-Infinity";
        return String.valueOf(value);
    }
}