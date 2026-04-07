package calculator.controller;

import calculator.converter.UnitConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller for unit conversion.
 */
@RestController
@RequestMapping("/api")
public class ConvertController {

    /**
     * Converts a value from one unit to another.
     * Example body: {"value": 100, "from": "meters", "to": "feet"}
     *
     * @param body the request body containing value, from and to fields
     * @return the converted value or an error message
     */
    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(@RequestBody Map<String, Object> body) {
        try {
            double value = ((Number) body.get("value")).doubleValue();
            String from  = (String) body.get("from");
            String to    = (String) body.get("to");

            if (from == null || to == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing 'from' or 'to' field"));
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
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}