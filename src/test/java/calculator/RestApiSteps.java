package calculator;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class RestApiSteps {

    private Response response;

    @Given("the REST API server is running on port {int}")
    public void initBaseUri(Integer port) {
        baseURI = "http://localhost:" + port;
    }

    @When("I send a GET request to {string} with expression {string}")
    public void sendGetRequest(String endpoint, String expression) {
        response = given()
                .param("expression", expression)
                .when()
                .get(endpoint);
    }

    @Then("the API returns status code {int}")
    public void assertStatusCode(Integer expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Then("the response contains result {int}")
    public void assertResult(Integer expectedResult) {
        int actualResult = response.jsonPath().getInt("result");
        assertEquals(expectedResult, actualResult);
    }

    @Then("the response contains an error message")
    public void assertErrorMessage() {
        assertNotNull(response.jsonPath().getString("error"));
    }
}