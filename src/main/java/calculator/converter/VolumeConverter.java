package calculator.converter;

import java.util.Map;

public class VolumeConverter {

    private static final Map<String, Double> TO_LITER = Map.of(
            "liter", 1.0,
            "milliliter", 0.001,
            "cubic_meter", 1000.0,
            "cubic_centimeter", 0.001,
            "cubic_millimeter", 0.000001,
            "gallon", 3.785411784,
            "quart", 0.946352946,
            "pint", 0.473176473,
            "cup", 0.2365882365,
            "fluid_ounce", 0.0295735295625
    );

    public static boolean supports(String unit) {
        return TO_LITER.containsKey(unit);
    }

    public static ConversionResult convert(double value, String from, String to) {
        if (!supports(from) || !supports(to)) {
            return new ConversionResult(
                    ConversionStatus.UNSUPPORTED_UNIT,
                    0,
                    "Unsupported unit: " + (!supports(from) ? from : to)
            );
        }

        double valueInLiters = value * TO_LITER.get(from);
        double convertedValue = valueInLiters / TO_LITER.get(to);

        return new ConversionResult(
                ConversionStatus.SUCCESS,
                convertedValue,
                null
        );
    }
}