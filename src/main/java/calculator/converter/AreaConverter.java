package calculator.converter;

import java.util.Map;

public class AreaConverter {

    private static final Map<String, Double> TO_SQUARE_METER = Map.of(
            "square_meter", 1.0,
            "square_kilometer", 1000000.0,
            "square_centimeter", 0.0001,
            "square_millimeter", 0.000001,
            "square_mile", 2589988.110336,
            "square_yard", 0.83612736,
            "square_foot", 0.09290304,
            "square_inch", 0.00064516
    );

    public static boolean supports(String unit) {
        return TO_SQUARE_METER.containsKey(unit);
    }

    public static ConversionResult convert(double value, String from, String to) {
        if (!supports(from) || !supports(to)) {
            return new ConversionResult(
                    ConversionStatus.UNSUPPORTED_UNIT,
                    0,
                    "Unsupported unit: " + (!supports(from) ? from : to)
            );
        }

        double valueInSquareMeters = value * TO_SQUARE_METER.get(from);
        double convertedValue = valueInSquareMeters / TO_SQUARE_METER.get(to);

        return new ConversionResult(
                ConversionStatus.SUCCESS,
                convertedValue,
                null
        );
    }
}