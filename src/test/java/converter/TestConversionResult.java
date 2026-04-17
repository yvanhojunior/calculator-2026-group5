package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestConversionResult {

    @Test
    void successResultExposesValueAndToString() {
        ConversionResult result = new ConversionResult(ConversionStatus.SUCCESS, 12.5, null);
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(12.5, result.getValue(), 1e-9);
        assertNull(result.getErrorMessage());
        assertEquals("12.5", result.toString());
    }

    @Test
    void errorResultExposesMessageAndToString() {
        ConversionResult result = new ConversionResult(ConversionStatus.UNSUPPORTED_UNIT, 0.0, "Unsupported unit");
        assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
        assertEquals(0.0, result.getValue(), 1e-9);
        assertEquals("Unsupported unit", result.getErrorMessage());
        assertEquals("Error: Unsupported unit", result.toString());
    }
}
