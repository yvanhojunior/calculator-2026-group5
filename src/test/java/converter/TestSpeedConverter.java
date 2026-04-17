package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.SpeedConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestSpeedConverter {

	@Test
	void supportsKnownUnits() {
		assertTrue(SpeedConverter.supports("knot"));
		assertFalse(SpeedConverter.supports("meter"));
	}

	@Test
	void convertKnotToKilometerPerHour() {
		ConversionResult result = SpeedConverter.convert(1.0, "knot", "kilometer_per_hour");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(1.852, result.getValue(), 1e-3);
	}

	@Test
	void convertReturnsUnsupportedUnit() {
		ConversionResult result = SpeedConverter.convert(1.0, "knot", "mph");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
