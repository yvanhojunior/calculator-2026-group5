package calculator.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LengthConverter {

    private static final Map<String, Double> TO_METERS = new HashMap<>();

    static {
        TO_METERS.put("meters", 1.0);
        TO_METERS.put("kilometers", 1000.0);
        TO_METERS.put("centimeters", 0.01);
        TO_METERS.put("decimeters", 0.1);
        TO_METERS.put("millimeters", 0.001);
        TO_METERS.put("feet", 0.3048);
        TO_METERS.put("inches", 0.0254);
        TO_METERS.put("miles", 1609.344);
        TO_METERS.put("yards", 0.9144);
        TO_METERS.put("nauticalmiles", 1852.0);
    }

    public static Set<String> getSupportedUnits() {
        return TO_METERS.keySet();
    }

    public static boolean supports(String unit) {
        return TO_METERS.containsKey(unit.toLowerCase());
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

        double meters = value * TO_METERS.get(fromNorm);
        double result = meters / TO_METERS.get(toNorm);

        return new ConversionResult(ConversionStatus.SUCCESS, result, null);
    }
}