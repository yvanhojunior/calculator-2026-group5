package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.UnitConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUnitConverter {

	@Test
	void convertsLengthCategory() {
		ConversionResult result = UnitConverter.convert(1000.0, "meters", "kilometers");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(1.0, result.getValue(), 1e-9);
	}

	@Test
	void convertsTemperatureCategory() {
		ConversionResult result = UnitConverter.convert(100.0, "celsius", "fahrenheit");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(212.0, result.getValue(), 1e-9);
	}

	@Test
	void returnsUnsupportedUnit() {
		ConversionResult result = UnitConverter.convert(1.0, "unknown", "meters");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertTrue(result.getErrorMessage().contains("Unsupported unit"));
	}

	@Test
	void returnsIncompatibleUnits() {
		ConversionResult result = UnitConverter.convert(1.0, "meters", "grams");
		assertEquals(ConversionStatus.INCOMPATIBLE_UNITS, result.getStatus());
		assertTrue(result.getErrorMessage().contains("Cannot convert"));
	}
}
