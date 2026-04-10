package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.LengthConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestLengthConverter {

	@Test
	void supportsIsCaseInsensitive() {
		assertTrue(LengthConverter.supports("METERS"));
		assertFalse(LengthConverter.supports("grams"));
	}

	@Test
	void convertKilometersToMeters() {
		ConversionResult result = LengthConverter.convert(2.0, "kilometers", "meters");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(2000.0, result.getValue(), 1e-9);
	}

	@Test
	void convertReturnsUnsupportedUnit() {
		ConversionResult result = LengthConverter.convert(2.0, "unknown", "meters");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
