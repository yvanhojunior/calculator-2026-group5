package calculator.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeightConverter {

    private static final Map<String, Double> TO_KILOGRAMS = new HashMap<>();

    static {
        TO_KILOGRAMS.put("kilograms", 1.0);
        TO_KILOGRAMS.put("grams", 0.001);
        TO_KILOGRAMS.put("milligrams", 0.000001);
        TO_KILOGRAMS.put("pounds", 0.453592);
        TO_KILOGRAMS.put("ounces", 0.0283495);
        TO_KILOGRAMS.put("tonnes", 1000.0);
    }

    public static Set<String> getSupportedUnits() {
        return TO_KILOGRAMS.keySet();
    }

    public static boolean supports(String unit) {
        return TO_KILOGRAMS.containsKey(unit.toLowerCase());
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

        double kg     = value * TO_KILOGRAMS.get(fromNorm);
        double result = kg / TO_KILOGRAMS.get(toNorm);

        return new ConversionResult(ConversionStatus.SUCCESS, result, null);
    }
}