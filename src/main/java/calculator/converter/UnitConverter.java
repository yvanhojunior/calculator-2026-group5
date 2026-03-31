package calculator.converter;

public class UnitConverter {

    /**
     * Converts a value from one unit to another.
     * Automatically detects the category (length, weight, temperature, area).
     */
    public static ConversionResult convert(double value, String from, String to) {
        String fromNorm = from.toLowerCase().replace(" ", "");
        String toNorm = to.toLowerCase().replace(" ", "");

        // Length
        if (LengthConverter.supports(fromNorm) && LengthConverter.supports(toNorm)) {
            return LengthConverter.convert(value, fromNorm, toNorm);
        }

        // Weight
        if (WeightConverter.supports(fromNorm) && WeightConverter.supports(toNorm)) {
            return WeightConverter.convert(value, fromNorm, toNorm);
        }

        // Temperature
        if (TemperatureConverter.supports(fromNorm) && TemperatureConverter.supports(toNorm)) {
            return TemperatureConverter.convert(value, fromNorm, toNorm);
        }

        // Area
        if (AreaConverter.supports(fromNorm) && AreaConverter.supports(toNorm)) {
            return AreaConverter.convert(value, fromNorm, toNorm);
        }

        // Unsupported unit
        if (!LengthConverter.supports(fromNorm)
                && !WeightConverter.supports(fromNorm)
                && !TemperatureConverter.supports(fromNorm)
                && !AreaConverter.supports(fromNorm)) {
            return new ConversionResult(
                    ConversionStatus.UNSUPPORTED_UNIT,
                    0,
                    "Unsupported unit: " + from
            );
        }

        // Incompatible units
        return new ConversionResult(
                ConversionStatus.INCOMPATIBLE_UNITS,
                0,
                "Cannot convert " + from + " to " + to + " (different categories)"
        );
    }
}