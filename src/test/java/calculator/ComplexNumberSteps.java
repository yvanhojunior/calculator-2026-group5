package calculator;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexNumberSteps {

    private ComplexNumber first;
    private ComplexNumber second;
    private ComplexNumber result;

    @Before
    public void reset() {
        first = null;
        second = null;
        result = null;
    }

    private ComplexNumber parse(String s) {
        // Gère formats : "2+3i", "2-3i", "-4+7i", "1.6-0.2i"
        s = s.replace(" ", "");
        int lastSign = Math.max(s.lastIndexOf('+'), s.lastIndexOf('-'));
        // cas où le signe est au début (nombre négatif réel)
        if (lastSign == 0) lastSign = Math.max(
                s.indexOf('+', 1), s.indexOf('-', 1)
        );
        double real = Double.parseDouble(s.substring(0, lastSign));
        double imaginary = Double.parseDouble(s.substring(lastSign, s.length() - 1));
        return new ComplexNumber(real, imaginary);
    }

    @Given("a complex number {string}")
    public void initComplexNumber(String s) {
        if (first == null) {
            first = parse(s);
        } else {
            second = parse(s);
        }
    }

    @When("I add the complex numbers")
    public void computeSum() {
        result = first.add(second);
    }

    @When("I subtract the complex numbers")
    public void computeDifference() {
        result = first.subtract(second);
    }

    @When("I multiply the complex numbers")
    public void computeProduct() {
        result = first.multiply(second);
    }

    @When("I divide the complex numbers")
    public void computeQuotient() {
        result = first.divide(second);
    }

    @Then("the complex result is {string}")
    public void assertComplexResult(String s) {
        ComplexNumber expected = parse(s);
        assertEquals(expected, result);
    }

    @Then("dividing complex numbers throws an ArithmeticException")
    public void assertDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> first.divide(second));
    }
}