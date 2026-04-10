package controller;

import calculator.controller.CalculatorController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestCalculatorController {

	private final CalculatorController controller = new CalculatorController();

	@Test
	void calculateReturnsResultForValidExpression() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("expression", "3+4");

		ResponseEntity<Map<String, Object>> response = controller.calculate(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("7", response.getBody().get("result"));
	}

	@Test
	void calculateReturnsBadRequestWhenExpressionMissing() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		ResponseEntity<Map<String, Object>> response = controller.calculate(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("errors.missing_expression_parameter", response.getBody().get("errorCode"));
	}

	@Test
	void calculateReturnsDivisionByZeroError() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("expression", "5/0");

		ResponseEntity<Map<String, Object>> response = controller.calculate(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("errors.division_by_zero", response.getBody().get("errorCode"));
	}

	@Test
	void calculateReturnsInvalidExpressionError() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("expression", "abc");

		ResponseEntity<Map<String, Object>> response = controller.calculate(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("errors.invalid_expression", response.getBody().get("errorCode"));
	}

	@Test
	void switchFormatReturnsDecimalValue() {
		Map<String, String> body = Map.of("numerator", "1", "denominator", "2");

		ResponseEntity<Map<String, Object>> response = controller.switchFormat(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("0.5", response.getBody().get("decimalValue"));
	}

	@Test
	void switchFormatReturnsMissingFieldError() {
		Map<String, String> body = Map.of("numerator", "1");

		ResponseEntity<Map<String, Object>> response = controller.switchFormat(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("errors.missing_numerator_or_denominator", response.getBody().get("errorCode"));
	}

	@Test
	void switchFormatReturnsNumberFormatError() {
		Map<String, String> body = Map.of("numerator", "x", "denominator", "2");

		ResponseEntity<Map<String, Object>> response = controller.switchFormat(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("errors.invalid_number_format", response.getBody().get("errorCode"));
	}

	@Test
	void pingReturnsServiceHealth() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/api/ping");

		ResponseEntity<Map<String, Object>> response = controller.ping(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UP", response.getBody().get("status"));
		assertEquals("calculator-api", response.getBody().get("service"));
		assertEquals("/api/ping", response.getBody().get("path"));
		assertNotNull(response.getBody().get("timestamp"));
	}
}
