package calculator.converter;

public class ConversionResult {

    private final ConversionStatus status;
    private final double value;
    private final String errorMessage;

    public ConversionResult(ConversionStatus status, double value, String errorMessage) {
        this.status = status;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public ConversionStatus getStatus() { return status; }
    public double getValue() { return value; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public String toString() {
        if (status == ConversionStatus.SUCCESS) return String.valueOf(value);
        return "Error: " + errorMessage;
    }
}