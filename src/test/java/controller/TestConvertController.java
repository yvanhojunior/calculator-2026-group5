package controller;

import calculator.controller.ConvertController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestConvertController {

	private final ConvertController controller = new ConvertController();

	@Test
	void convertReturnsSuccessForValidUnits() {
		Map<String, Object> body = new HashMap<>();
		body.put("value", 1000.0);
		body.put("from", "meters");
		body.put("to", "kilometers");

		ResponseEntity<Map<String, Object>> response = controller.convert(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("SUCCESS", response.getBody().get("status"));
		assertEquals("meters", response.getBody().get("from"));
		assertEquals("kilometers", response.getBody().get("to"));
		assertEquals(1.0, (Double) response.getBody().get("value"), 1e-9);
	}

	@Test
	void convertReturnsUnsupportedUnit() {
		Map<String, Object> body = new HashMap<>();
		body.put("value", 1.0);
		body.put("from", "unknown");
		body.put("to", "meters");

		ResponseEntity<Map<String, Object>> response = controller.convert(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UNSUPPORTED_UNIT", response.getBody().get("status"));
	}

	@Test
	void convertReturnsIncompatibleUnits() {
		Map<String, Object> body = new HashMap<>();
		body.put("value", 1.0);
		body.put("from", "meters");
		body.put("to", "grams");

		ResponseEntity<Map<String, Object>> response = controller.convert(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("INCOMPATIBLE_UNITS", response.getBody().get("status"));
	}

	@Test
	void convertReturnsMissingFieldError() {
		Map<String, Object> body = new HashMap<>();
		body.put("value", 1.0);
		body.put("from", "meters");

		ResponseEntity<Map<String, Object>> response = controller.convert(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Missing 'from' or 'to' field", response.getBody().get("error"));
	}

	@Test
	void convertReturnsInvalidRequestForBadPayload() {
		Map<String, Object> body = new HashMap<>();
		body.put("from", "meters");
		body.put("to", "kilometers");

		ResponseEntity<Map<String, Object>> response = controller.convert(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(String.valueOf(response.getBody().get("error")).startsWith("Invalid request:"));
	}
}
