package calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Representation of a real number using BigDecimal for arbitrary precision.
 * Supports configurable precision via MathContext.
 *
 * Special cases:
 * - Division by zero throws ArithmeticException
 * - NaN and Infinity are represented as special string constants
 */
public class RealValue implements NumberValue {

    // Précision par défaut: 10 chiffres significatifs
    public static final MathContext DEFAULT_PRECISION = new MathContext(10, RoundingMode.HALF_UP);

    private final BigDecimal value;
    private final MathContext precision;

    // Constantes spéciales
    public static final RealValue NAN      = new RealValue("NaN");
    public static final RealValue POS_INF  = new RealValue("+Infinity");
    public static final RealValue NEG_INF  = new RealValue("-Infinity");
    public static final RealValue PI       = new RealValue(new BigDecimal(Math.PI), DEFAULT_PRECISION);
    public static final RealValue E        = new RealValue(new BigDecimal(Math.E),  DEFAULT_PRECISION);

    private final String specialValue; // null si ce n'est pas une valeur spéciale

    // Constructeur normal
    public RealValue(BigDecimal value, MathContext precision) {
        this.value = value.round(precision);
        this.precision = precision;
        this.specialValue = null;
    }

    // Constructeur avec double (commodité)
    public RealValue(double value) {
        this(BigDecimal.valueOf(value), DEFAULT_PRECISION);
    }

    // Constructeur pour valeurs spéciales (NaN, +Infinity, -Infinity)
    private RealValue(String specialValue) {
        this.value = null;
        this.precision = DEFAULT_PRECISION;
        this.specialValue = specialValue;
    }

    public boolean isSpecial() { return specialValue != null; }
    public boolean isNaN()     { return "NaN".equals(specialValue); }
    public boolean isPosInf()  { return "+Infinity".equals(specialValue); }
    public boolean isNegInf()  { return "-Infinity".equals(specialValue); }

    public BigDecimal getValue() { return value; }

    @Override
    public NumberValue add(NumberValue other) {
        if (other instanceof ComplexValue o) {
            return new ComplexValue(value.doubleValue(), 0).add(o);
        }
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return handleSpecialAdd(o);
        return new RealValue(value.add(o.value), precision);
    }

    @Override
    public NumberValue sub(NumberValue other) {
        if (other instanceof ComplexValue o) {
            return new ComplexValue(value.doubleValue(), 0).sub(o);
        }
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return handleSpecialSub(o);
        return new RealValue(value.subtract(o.value), precision);
    }

    @Override
    public NumberValue mul(NumberValue other) {
        if (other instanceof ComplexValue o) {
            return new ComplexValue(value.doubleValue(), 0).mul(o);
        }
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return handleSpecialMul(o);
        return new RealValue(value.multiply(o.value, precision), precision);
    }

    @Override
    public NumberValue div(NumberValue other) {
        if (other instanceof ComplexValue o) {
            return new ComplexValue(value.doubleValue(), 0).div(o);
        }
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return handleSpecialDiv(o);
        if (o.value.compareTo(BigDecimal.ZERO) == 0) {
            // Comme en réel: 1/0 = +Inf, -1/0 = -Inf, 0/0 = NaN
            if (value.compareTo(BigDecimal.ZERO) == 0) return NAN;
            return value.signum() > 0 ? POS_INF : NEG_INF;
        }
        return new RealValue(value.divide(o.value, precision), precision);
    }

    @Override
    public NumberValue pow(NumberValue other) {
        if (other instanceof RationalValue o) {
            double exp = (double) o.getNumerator() / o.getDenominator();
            return new RealValue(Math.pow(value.doubleValue(), exp));
        }
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return NAN;
        return new RealValue(Math.pow(value.doubleValue(), o.getValue().doubleValue()));
    }

    @Override
    public NumberValue mod(NumberValue other) {
        RealValue o = toReal(other);
        if (isSpecial() || o.isSpecial()) return NAN;
        if (o.value.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Modulo by zero is not allowed.");
        }
        BigDecimal result = value.remainder(o.value, precision);
        // Le résultat du modulo doit toujours être positif ou nul
        if (result.signum() < 0) {
            result = result.add(o.value, precision);
        }
        return new RealValue(result, precision);
    }

    // Gestion des cas spéciaux pour l'addition
    private RealValue handleSpecialAdd(RealValue o) {
        if (isNaN() || o.isNaN()) return NAN;
        if (isPosInf() && o.isNegInf()) return NAN;
        if (isNegInf() && o.isPosInf()) return NAN;
        if (isPosInf() || o.isPosInf()) return POS_INF;
        if (isNegInf() || o.isNegInf()) return NEG_INF;
        return NAN;
    }

    private RealValue handleSpecialSub(RealValue o) {
        if (isNaN() || o.isNaN()) return NAN;
        if (isPosInf() && o.isPosInf()) return NAN;
        if (isNegInf() && o.isNegInf()) return NAN;
        if (isPosInf()) return POS_INF;
        if (isNegInf()) return NEG_INF;
        if (o.isPosInf()) return NEG_INF;
        if (o.isNegInf()) return POS_INF;
        return NAN;
    }

    private RealValue handleSpecialMul(RealValue o) {
        if (isNaN() || o.isNaN()) return NAN;
        if ((isPosInf() || isNegInf()) && !o.isSpecial() && o.value.compareTo(BigDecimal.ZERO) == 0) return NAN;
        if (!isSpecial() && value.compareTo(BigDecimal.ZERO) == 0 && (o.isPosInf() || o.isNegInf())) return NAN;
        boolean negative = (isNegInf() || (!isSpecial() && value.signum() < 0)) ^
                (o.isNegInf() || (!o.isSpecial() && o.value.signum() < 0));
        return negative ? NEG_INF : POS_INF;
    }

    private RealValue handleSpecialDiv(RealValue o) {
        if (isNaN() || o.isNaN()) return NAN;
        if ((isPosInf() || isNegInf()) && (o.isPosInf() || o.isNegInf())) return NAN;
        if (isPosInf() || isNegInf()) {
            boolean negative = isNegInf() ^ (o.isSpecial() ? false : o.value.signum() < 0);
            return negative ? NEG_INF : POS_INF;
        }
        return NAN;
    }

    @Override
    public String toString() {
        if (isSpecial()) return specialValue;
        // toPlainString évite la notation scientifique
        // on garde toujours au moins une décimale pour distinguer les réels des entiers
        String plain = value.stripTrailingZeros().toPlainString();
        if (!plain.contains(".")) {
            plain = plain + ".0";
        }
        return plain;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof RealValue)) return false;
        RealValue other = (RealValue) o;
        if (isSpecial() && other.isSpecial()) return specialValue.equals(other.specialValue);
        if (isSpecial() || other.isSpecial()) return false;
        return value.compareTo(other.value) == 0;
    }

    @Override
    public int hashCode() {
        if (isSpecial()) return specialValue.hashCode();
        return value.stripTrailingZeros().hashCode();
    }

    private RealValue toReal(NumberValue other) {
        if (other instanceof RealValue) return (RealValue) other;
        if (other instanceof RationalValue) {
            RationalValue r = (RationalValue) other;
            return new RealValue(new BigDecimal(r.getNumerator()).divide(new BigDecimal(r.getDenominator()), precision), precision);
        }
        if (other instanceof IntegerValue)
            return new RealValue(((IntegerValue) other).getValue());
        throw new IllegalArgumentException("Cannot convert " + other.getClass() + " to RealValue");
    }
}