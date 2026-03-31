package calculator.converter;

import java.util.Set;

public class TemperatureConverter {

    private static final Set<String> SUPPORTED = Set.of("celsius", "fahrenheit", "kelvin");

    public static boolean supports(String unit) {
        return SUPPORTED.contains(unit.toLowerCase());
    }

    public static Set<String> getSupportedUnits() {
        return SUPPORTED;
    }

    public static ConversionResult convert(double value, String from, String to) {
        String fromNorm = from.toLowerCase();
        String toNorm   = to.toLowerCase();

        if (!supports(fromNorm)) {
            return new ConversionResult(ConversionStatus.UNSUPPORTED_UNIT, 0,
                    "Unsupported unit: " + from);
        }
        if (!supports(toNorm)) {
            return new ConversionResult(ConversionStatus.UNSUPPORTED_UNIT, 0,
                    "Unsupported unit: " + to);
        }

        // Convert to Celsius first
        double celsius = switch (fromNorm) {
            case "celsius"    -> value;
            case "fahrenheit" -> (value - 32) * 5.0 / 9.0;
            case "kelvin"     -> value - 273.15;
            default           -> throw new IllegalArgumentException("Unsupported unit: " + from);
        };

        // Convert from Celsius to target
        double result = switch (toNorm) {
            case "celsius"    -> celsius;
            case "fahrenheit" -> celsius * 9.0 / 5.0 + 32;
            case "kelvin"     -> celsius + 273.15;
            default           -> throw new IllegalArgumentException("Unsupported unit: " + to);
        };

        return new ConversionResult(ConversionStatus.SUCCESS, result, null);
    }
}