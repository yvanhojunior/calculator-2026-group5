package calculator.converter;

import java.util.Map;

public class SpeedConverter {

    private static final Map<String, Double> TO_MPS = Map.of(
            "meter_per_second", 1.0,
            "kilometer_per_hour", 0.2777777778,
            "mile_per_hour", 0.44704,
            "foot_per_second", 0.3048,
            "knot", 0.514444
    );

    public static boolean supports(String unit) {
        return TO_MPS.containsKey(unit);
    }

    public static ConversionResult convert(double value, String from, String to) {
        if (!supports(from) || !supports(to)) {
            return new ConversionResult(
                    ConversionStatus.UNSUPPORTED_UNIT,
                    0,
                    "Unsupported unit: " + (!supports(from) ? from : to)
            );
        }

        double valueInMps = value * TO_MPS.get(from);
        double converted = valueInMps / TO_MPS.get(to);

        return new ConversionResult(
                ConversionStatus.SUCCESS,
                converted,
                null
        );
    }
}