package calculator;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class LinearEquationSteps {

    private String equation;
    private List<String> equations;
    private LinearEquationSolver.Result result;

    @Before
    public void reset() {
        equation = null;
        equations = null;
        result = null;
    }

    @Given("the linear equation {string}")
    public void givenLinearEquation(String equation) {
        this.equation = equation;
    }

    @Given("the linear equations:")
    public void givenLinearEquations(DataTable dataTable) {
        this.equations = dataTable.asList();
    }

    @When("I solve the equation")
    public void solveEquation() {
        result = LinearEquationSolver.solve(equation);
    }

    @When("I solve the system")
    public void solveSystem() {
        result = LinearEquationSolver.solveSystem(equations);
    }

    @Then("the solution is {string}")
    public void assertSolution(String expected) {
        assertEquals(LinearEquationSolver.SolutionType.UNIQUE, result.getType());
        for (String part : expected.split(",")) {
            String[] kv = part.trim().split("=");
            String var = kv[0].trim();
            double val = Double.parseDouble(kv[1].trim());
            assertEquals(val, result.getSolutions().get(var), 1e-9);
        }
    }

    @Then("there is no solution")
    public void assertNoSolution() {
        assertEquals(LinearEquationSolver.SolutionType.NO_SOLUTION, result.getType());
    }

    @Then("there are infinite solutions")
    public void assertInfiniteSolutions() {
        assertEquals(LinearEquationSolver.SolutionType.INFINITE_SOLUTIONS, result.getType());
    }

    @Then("there is a syntax error")
    public void assertSyntaxError() {
        assertEquals(LinearEquationSolver.SolutionType.SYNTAX_ERROR, result.getType());
    }
}