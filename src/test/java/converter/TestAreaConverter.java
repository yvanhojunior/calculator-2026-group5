package converter;

import calculator.converter.AreaConverter;
import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAreaConverter {

	@Test
	void supportsKnownUnits() {
		assertTrue(AreaConverter.supports("square_meter"));
		assertTrue(AreaConverter.supports("square_foot"));
		assertFalse(AreaConverter.supports("meters"));
	}

	@Test
	void convertSquareMeterToSquareCentimeter() {
		ConversionResult result = AreaConverter.convert(1.0, "square_meter", "square_centimeter");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(10000.0, result.getValue(), 1e-9);
	}

	@Test
	void convertReturnsUnsupportedForInvalidUnit() {
		ConversionResult result = AreaConverter.convert(1.0, "square_meter", "unknown");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
