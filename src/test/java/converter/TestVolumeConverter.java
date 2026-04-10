package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.VolumeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestVolumeConverter {

	@Test
	void supportsKnownUnits() {
		assertTrue(VolumeConverter.supports("liter"));
		assertTrue(VolumeConverter.supports("gallon"));
		assertFalse(VolumeConverter.supports("meters"));
	}

	@Test
	void convertGallonToLiter() {
		ConversionResult result = VolumeConverter.convert(1.0, "gallon", "liter");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(3.785411784, result.getValue(), 1e-9);
	}

	@Test
	void convertReturnsUnsupportedUnit() {
		ConversionResult result = VolumeConverter.convert(1.0, "liter", "unknown");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
