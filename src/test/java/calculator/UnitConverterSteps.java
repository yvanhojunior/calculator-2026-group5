package calculator;

import calculator.converter.ConversionResult;
import calculator.converter.ConversionStatus;
import calculator.converter.UnitConverter;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class UnitConverterSteps {

    private double inputValue;
    private String fromUnit;
    private ConversionResult result;

    @Before
    public void reset() {
        inputValue = 0;
        fromUnit = null;
        result = null;
    }

    @Given("I have a value of {double} in {string}")
    public void initValue(Double value, String unit) {
        this.inputValue = value;
        this.fromUnit = unit;
    }

    @When("I convert to {string}")
    public void convertTo(String toUnit) {
        result = UnitConverter.convert(inputValue, fromUnit, toUnit);
    }

    @Then("the converted value is {double}")
    public void assertConvertedValue(Double expected) {
        assertEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertEquals(expected, result.getValue(), 1e-6);
    }

    @Then("there is a conversion error")
    public void assertConversionError() {
        assertNotEquals(ConversionStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getErrorMessage());
    }
}