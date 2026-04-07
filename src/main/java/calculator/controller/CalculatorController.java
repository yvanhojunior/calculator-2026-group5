package calculator.controller;
import calculator.LinearEquationSolver;
import java.util.List;

import calculator.*;
import calculator.converter.UnitConverter;
import calculator.parser.ExpressionParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController("queryCalculatorController")
@RequestMapping("/api")
public class CalculatorController {

    private final Calculator calculator = new Calculator();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Path CONVERSION_LIST_PATH = Paths.get("src", "main", "resources", "static",
            "conversion_list.json");


    @Value("${api.key:}")
    private String apiKey;

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(HttpServletRequest request) {
        try {
            String rawQuery = request.getQueryString();
            String expression = rawQuery.replace("expression=", "");
            // Decode all URL-encoded operators
            expression = expression.replace("%2B", "+");
            expression = expression.replace("%2F", "/");
            expression = expression.replace("%2A", "*");
            expression = expression.replace("%2D", "-");
            expression = expression.replace("%5E", "^");
            Expression expr = calculator.read(expression);
            NumberValue result = calculator.eval(expr);
            return ResponseEntity.ok(Map.of("result", result.toString()));
        } catch (ArithmeticException e) {
            return ResponseEntity
				.badRequest()
				.body(Map.of("error", "Division by zero"));
        } catch (Exception e) {
            return ResponseEntity
				.badRequest()
				.body(Map.of("error", "Invalid expression: " + e.getMessage()));
        }
    }

    @PostMapping("/solve")
    public ResponseEntity<Map<String, Object>> solve(@RequestBody Map<String, Object> body) {
        try {
            String equation = (String) body.get("equation");

            if (equation == null || equation.isEmpty()) {
                return ResponseEntity.badRequest()
					.body(Map.of("error", "Missing 'equation' field"));
            }

            LinearEquationSolver.Result result = LinearEquationSolver.solve(equation);

            return switch (result.getType()) {
                case UNIQUE -> ResponseEntity.ok(Map.of(
					"type", "UNIQUE",
					"solutions", result.getSolutions()
                ));
                case NO_SOLUTION -> ResponseEntity.ok(Map.of(
					"type", "NO_SOLUTION",
					"message", result.getErrorMessage()
                ));
                case INFINITE_SOLUTIONS -> ResponseEntity.ok(Map.of(
					"type", "INFINITE_SOLUTIONS",
					"message", result.getErrorMessage()
                ));
                case SYNTAX_ERROR -> ResponseEntity.badRequest().body(Map.of(
					"type", "SYNTAX_ERROR",
					"error", result.getErrorMessage()
                ));
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest()
				.body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    @PostMapping("/solve/system")
    public ResponseEntity<Map<String, Object>> solveSystem(@RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            List<String> equations = (List<String>) body.get("equations");

            if (equations == null || equations.isEmpty()) {
                return ResponseEntity.badRequest()
					.body(Map.of("error", "Missing 'equations' field"));
            }

            LinearEquationSolver.Result result = LinearEquationSolver.solveSystem(equations);

            return switch (result.getType()) {
                case UNIQUE -> ResponseEntity.ok(Map.of(
					"type", "UNIQUE",
					"solutions", result.getSolutions()
                ));
                case NO_SOLUTION -> ResponseEntity.ok(Map.of(
					"type", "NO_SOLUTION",
					"message", result.getErrorMessage()
                ));
                case INFINITE_SOLUTIONS -> ResponseEntity.ok(Map.of(
					"type", "INFINITE_SOLUTIONS",
					"message", result.getErrorMessage()
                ));
                case SYNTAX_ERROR -> ResponseEntity.badRequest().body(Map.of(
					"type", "SYNTAX_ERROR",
					"error", result.getErrorMessage()
                ));
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest()
				.body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(@RequestBody Map<String, Object> body) {
        try {
            double value = ((Number) body.get("value")).doubleValue();
            String from  = (String) body.get("from");
            String to    = (String) body.get("to");

            if (from == null || to == null) {
                return ResponseEntity.badRequest()
					.body(Map.of("error", "Missing 'from' or 'to' field"));
            }

            calculator.converter.ConversionResult result = UnitConverter.convert(value, from, to);

            return switch (result.getStatus()) {
                case SUCCESS -> ResponseEntity.ok(Map.of(
					"status", "SUCCESS",
					"value", result.getValue(),
					"from", from,
					"to", to
                ));
                case UNSUPPORTED_UNIT -> ResponseEntity.badRequest().body(Map.of(
					"status", "UNSUPPORTED_UNIT",
					"error", result.getErrorMessage()
                ));
                case INCOMPATIBLE_UNITS -> ResponseEntity.badRequest().body(Map.of(
					"status", "INCOMPATIBLE_UNITS",
					"error", result.getErrorMessage()
                ));
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest()
				.body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    @GetMapping("/currencies")
    public ResponseEntity<Map<String, Object>> getCurrencies() {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Missing API key. Define API_KEY in .env"
                ));
            }

            String apiUrl = String.format(
                "https://v6.exchangerate-api.com/v6/%s/latest/EUR",
                apiKey.trim()
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> apiResponse = restTemplate.getForObject(apiUrl, Map.class);

            if (apiResponse == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Empty response from exchange API"
                ));
            }

            String result = String.valueOf(apiResponse.get("result"));
            if (!"success".equalsIgnoreCase(result)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Exchange API error: " + apiResponse.get("error-type")
                ));
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> apiRates = (Map<String, Object>) apiResponse.get("conversion_rates");
            if (apiRates == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Missing conversion_rates in API response"
                ));
            }

            Map<String, Object> conversionList = objectMapper.readValue(
                Files.readString(CONVERSION_LIST_PATH),
                new TypeReference<Map<String, Object>>() {
                }
            );

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> currencies = (List<Map<String, Object>>) conversionList.get("currencies");
            if (currencies == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Invalid conversion_list.json: currencies array is missing"
                ));
            }

            List<Map<String, Object>> updatedCurrencies = currencies.stream().map(currency -> {
                Map<String, Object> mutableCurrency = new LinkedHashMap<>(currency);
                Object codeValue = mutableCurrency.get("code");

                if (codeValue instanceof String code) {
                    Object rateValue = apiRates.get(code.toUpperCase());
                    if (rateValue instanceof Number n) {
                        mutableCurrency.put("rate", n.doubleValue());
                    }
                }

                return mutableCurrency;
            }).toList();

            long lastUpdated = toLong(apiResponse.get("time_last_update_unix"));
            long nextUpdate = toLong(apiResponse.get("time_next_update_unix"));

            conversionList.put("currencies", updatedCurrencies);
            conversionList.put("last_updated", lastUpdated);
            conversionList.put("next_update", nextUpdate);

            Files.writeString(
                CONVERSION_LIST_PATH,
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conversionList)
            );

            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "currencies", updatedCurrencies,
                "last_updated", lastUpdated,
                "next_update", nextUpdate
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "ERROR",
                "error", "Failed to fetch currency data: " + e.getMessage()
            ));
        }
    }

    private long toLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        if (value instanceof String s) {
            return Long.parseLong(s);
        }
        throw new IllegalArgumentException("Invalid unix timestamp value: " + value);
    }


}