package converter;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.TemperatureConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestTemperatureConverter {

	@Test
	void supportsKnownUnits() {
		assertTrue(TemperatureConverter.supports("celsius"));
		assertTrue(TemperatureConverter.supports("KELVIN"));
		assertFalse(TemperatureConverter.supports("meters"));
	}

	@Test
	void convertCelsiusToKelvin() {
		ConversionResult result = TemperatureConverter.convert(0.0, "celsius", "kelvin");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(273.15, result.getValue(), 1e-9);
	}

    @Test
    void convertCelsiusToFahrenheit() {
        ConversionResult result = TemperatureConverter.convert(0.0, "celsius", "fahrenheit");
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(32.0, result.getValue(), 1e-9);
    }

	@Test
	void convertFahrenheitToCelsius() {
		ConversionResult result = TemperatureConverter.convert(32.0, "fahrenheit", "celsius");
		assertEquals(ConversionStatus.SUCCESS, result.getStatus());
		assertEquals(0.0, result.getValue(), 1e-9);
	}

    @Test
    void convertFahrenheitToKelvin() {
        ConversionResult result = TemperatureConverter.convert(32.0, "fahrenheit", "kelvin");
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(273.15, result.getValue(), 1e-9);
    }

    @Test
    void convertKelvinToCelsius() {
        ConversionResult result = TemperatureConverter.convert(273.15, "kelvin", "celsius");
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(0.0, result.getValue(), 1e-9);
    }

    @Test
    void convertKelvinToFahrenheit() {
        ConversionResult result = TemperatureConverter.convert(273.15, "kelvin", "fahrenheit");
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(32.0, result.getValue(), 1e-9);
    }

	@Test
	void convertReturnsUnsupportedUnit() {
		ConversionResult result = TemperatureConverter.convert(32.0, "rankine", "celsius");
		assertEquals(ConversionStatus.UNSUPPORTED_UNIT, result.getStatus());
		assertNotNull(result.getErrorMessage());
	}
}
