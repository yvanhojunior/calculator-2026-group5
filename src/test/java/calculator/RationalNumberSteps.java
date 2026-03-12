package calculator;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class RationalNumberSteps {

    private RationalNumber first;
    private RationalNumber second;
    private RationalNumber result;

    @Given("a rational number {int}\\/{int}")
    public void initRational(Integer numerator, Integer denominator) {
        if (first == null) {
            first = new RationalNumber(numerator, denominator);
        } else {
            second = new RationalNumber(numerator, denominator);
        }
    }

    @When("I add them")
    public void computeSum() {
        result = first.add(second);
    }

    @When("I subtract them")
    public void computeDifference() {
        result = first.subtract(second);
    }

    @When("I multiply them")
    public void computeProduct() {
        result = first.multiply(second);
    }

    @When("I divide them")
    public void computeQuotient() {
        result = first.divide(second);
    }

    @Then("the result is the rational number {int}\\/{int}")
    public void assertRationalResult(Integer numerator, Integer denominator) {
        RationalNumber expected = new RationalNumber(numerator, denominator);
        assertEquals(expected, result);
    }

    @Then("dividing them throws an ArithmeticException")
    public void assertDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> first.divide(second));
    }
}