package calculator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * REST controller for currency conversion rates.
 */
@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.key:}")
    private String apiKey;

    /**
     * Fetches latest currency exchange rates from the external API
     * and returns the API response.
     *
     * @return the API response or an error message
     */
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

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "error", "Failed to fetch currency data: " + e.getMessage()
            ));
        }
    }
}