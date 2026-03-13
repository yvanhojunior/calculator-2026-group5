package calculator;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class RealNumberSteps {

    private RealNumber first;
    private RealNumber second;
    private RealNumber result;

    @Before
    public void reset() {
        first = null;
        second = null;
        result = null;
    }

    @Given("a real number {double}")
    public void initRealNumber(Double value) {
        if (first == null) {
            first = new RealNumber(value);
        } else {
            second = new RealNumber(value);
        }
    }

    @When("I add the real numbers")
    public void computeSum() {
        result = first.add(second);
    }

    @When("I subtract the real numbers")
    public void computeDifference() {
        result = first.subtract(second);
    }

    @When("I multiply the real numbers")
    public void computeProduct() {
        result = first.multiply(second);
    }

    @When("I divide the real numbers")
    public void computeQuotient() {
        result = first.divide(second);
    }

    @When("I take the square root of the real number")
    public void computeSqrt() {
        result = first.sqrt();
    }

    @Then("the real result is {double}")
    public void assertRealResult(Double expected) {
        assertEquals(new RealNumber(expected), result);
    }

    @Then("the real result is NaN")
    public void assertNaN() {
        assertTrue(Double.isNaN(result.getValue()));
    }

    @Then("the real result is Infinity")
    public void assertInfinity() {
        assertTrue(Double.isInfinite(result.getValue()) && result.getValue() > 0);
    }

    @Then("the real result is -Infinity")
    public void assertNegativeInfinity() {
        assertTrue(Double.isInfinite(result.getValue()) && result.getValue() < 0);
    }
}