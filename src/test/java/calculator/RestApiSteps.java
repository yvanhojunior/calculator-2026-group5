package calculator;

import io.cucumber.java.en.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestApiSteps {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpResponse<String> response;

    @Given("the REST API server is running on port {int}")
    public void initBaseUri(Integer port) {
        this.baseUri = "http://localhost:" + port;
    }

    private String baseUri;

    @When("I send a GET request to {string} with expression {string}")
    public void sendGetRequest(String endpoint, String expression) {
        try {
            String encodedExpression = URLEncoder.encode(expression, StandardCharsets.UTF_8);
            URI uri = URI.create(baseUri + endpoint + "?expression=" + encodedExpression);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            fail("Failed to send HTTP request: " + e.getMessage());
        }
    }

    @Then("the API returns status code {int}")
    public void assertStatusCode(Integer expectedStatus) {
        assertNotNull(response);
        assertEquals(expectedStatus.intValue(), response.statusCode());
    }

    @Then("the response contains result {int}")
    public void assertResult(Integer expectedResult) {
        assertNotNull(response);
        try {
            Map<String, Object> body = objectMapper.readValue(
                response.body(),
                new TypeReference<Map<String, Object>>() {}
            );
            Object result = body.get("result");
            assertNotNull(result);
            assertEquals(expectedResult.intValue(), Integer.parseInt(String.valueOf(result)));
        } catch (Exception e) {
            fail("Failed to parse response body: " + e.getMessage());
        }
    }

    @Then("the response contains an error message")
    public void assertErrorMessage() {
        assertNotNull(response);
        try {
            Map<String, Object> body = objectMapper.readValue(
                response.body(),
                new TypeReference<Map<String, Object>>() {}
            );
            assertNotNull(body.get("error"));
        } catch (Exception e) {
            fail("Failed to parse response body: " + e.getMessage());
        }
    }
}