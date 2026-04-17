package controller;

import calculator.controller.CurrencyController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestCurrencyController {

	@Test
	void getCurrenciesReturnsErrorWhenApiKeyMissing() {
		CurrencyController controller = new CurrencyController();
		ReflectionTestUtils.setField(controller, "apiKey", "");

		ResponseEntity<Map<String, Object>> response = controller.getCurrencies();

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("ERROR", response.getBody().get("status"));
		assertEquals("Missing API key. Define API_KEY in .env", response.getBody().get("error"));
	}

	@Test
	void getCurrenciesReturnsErrorWhenRemoteCallFails() {
		CurrencyController controller = new CurrencyController();
		// Keep a non-empty key so controller attempts the remote call and reaches catch block in unit test environment.
		ReflectionTestUtils.setField(controller, "apiKey", "dummy-key");

		ResponseEntity<Map<String, Object>> response = controller.getCurrencies();

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("ERROR", response.getBody().get("status"));
		assertTrue(String.valueOf(response.getBody().get("error")).startsWith("Failed to fetch currency data:"));
	}
}
