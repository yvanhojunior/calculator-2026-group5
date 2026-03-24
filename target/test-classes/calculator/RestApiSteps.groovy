package calculator;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class RestApiSteps {

    private Response response;

    @Given("the REST API server is running on port {int}")
    public void the_rest_api_server_is_running_on_port(Integer port) {
        baseURI = "http://localhost:" + port;
    }

    @When("I send a GET request to {string} with expression {string}")
    public void i_send_a_get_request_to_with_expression(String endpoint, String expression) {
        response = given()
                .param("expression", expression)
                .when()
                .get(endpoint);
    }

    @Then("the response status is {int}")
    public void the_response_status_is(Integer expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Then("the response contains result {int}")
    public void the_response_contains_result(Integer expectedResult) {
        int actualResult = response.jsonPath().getInt("result");
        assertEquals(expectedResult, actualResult);
    }

    @Then("the response contains an error message")
    public void the_response_contains_an_error_message() {
        assertNotNull(response.jsonPath().getString("error"));
    }
}