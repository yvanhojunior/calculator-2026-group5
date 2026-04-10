package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.WeightConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestWeightConverter {

	@Test
	void supportsKnownUnits() {
		assertTrue(WeightConverter.supports("kilograms"));
		assertFalse(WeightConverter.supports("meters"));
	}

	@Test
	void convertKilogramsToGrams() {
		ConversionResult result = WeightConverter.convert(1.0, "kilograms", "grams");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(1000.0, result.getValue(), 1e-9);
	}

	@Test
	void convertReturnsUnsupportedUnit() {
		ConversionResult result = WeightConverter.convert(1.0, "unknown", "grams");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
