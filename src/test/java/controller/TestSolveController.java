package controller;

import calculator.controller.SolveController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestSolveController {

	private final SolveController controller = new SolveController();

	@Test
	void solveReturnsUniqueSolution() {
		Map<String, Object> body = Map.of("equation", "2x + 3 = 7");

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UNIQUE", response.getBody().get("type"));
		@SuppressWarnings("unchecked")
		Map<String, Double> solutions = (Map<String, Double>) response.getBody().get("solutions");
		assertEquals(2.0, solutions.get("x"), 1e-9);
	}

	@Test
	void solveReturnsNoSolution() {
		Map<String, Object> body = Map.of("equation", "0x + 3 = 7");

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("NO_SOLUTION", response.getBody().get("type"));
	}

	@Test
	void solveReturnsInfiniteSolutions() {
		Map<String, Object> body = Map.of("equation", "0x + 0 = 0");

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("INFINITE_SOLUTIONS", response.getBody().get("type"));
	}

	@Test
	void solveReturnsSyntaxError() {
		Map<String, Object> body = Map.of("equation", "2x + = 7");

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("SYNTAX_ERROR", response.getBody().get("type"));
	}

	@Test
	void solveReturnsMissingEquationError() {
		Map<String, Object> body = new HashMap<>();

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Missing 'equation' field", response.getBody().get("error"));
	}

	@Test
	void solveReturnsInvalidRequestOnBadPayloadType() {
		Map<String, Object> body = new HashMap<>();
		body.put("equation", 42);

		ResponseEntity<Map<String, Object>> response = controller.solve(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(String.valueOf(response.getBody().get("error")).startsWith("Invalid request:"));
	}

	@Test
	void solveSystemReturnsUniqueSolution() {
		Map<String, Object> body = Map.of("equations", List.of("2x + y = 5", "x - y = 1"));

		ResponseEntity<Map<String, Object>> response = controller.solveSystem(body);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UNIQUE", response.getBody().get("type"));
		@SuppressWarnings("unchecked")
		Map<String, Double> solutions = (Map<String, Double>) response.getBody().get("solutions");
		assertEquals(2.0, solutions.get("x"), 1e-9);
		assertEquals(1.0, solutions.get("y"), 1e-9);
	}

	@Test
	void solveSystemReturnsMissingEquationsError() {
		Map<String, Object> body = new HashMap<>();

		ResponseEntity<Map<String, Object>> response = controller.solveSystem(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Missing 'equations' field", response.getBody().get("error"));
	}

	@Test
	void solveSystemReturnsInvalidRequestOnBadPayloadType() {
		Map<String, Object> body = new HashMap<>();
		body.put("equations", "x+y=2");

		ResponseEntity<Map<String, Object>> response = controller.solveSystem(body);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(String.valueOf(response.getBody().get("error")).startsWith("Invalid request:"));
	}
}
