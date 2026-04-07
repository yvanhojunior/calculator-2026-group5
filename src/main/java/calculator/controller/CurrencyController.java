package calculator.controller;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for currency conversion rates.
 */
@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Path CONVERSION_LIST_PATH = Paths.get("src", "main", "resources", "static",
            "conversion_list.json");

    @Value("${api.key:}")
    private String apiKey;

    /**
     * Fetches latest currency exchange rates from the external API
     * and updates the local conversion_list.json file.
     *
     * @return the updated list of currencies with their rates or an error message
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
                    new TypeReference<Map<String, Object>>() {}
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
            long nextUpdate  = toLong(apiResponse.get("time_next_update_unix"));

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
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) return Long.parseLong(s);
        throw new IllegalArgumentException("Invalid unix timestamp value: " + value);
    }
}