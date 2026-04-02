Feature: Unit Converter

  Scenario: Convert meters to kilometers
    Given I have a value of 1000.0 in "meters"
    When I convert to "kilometers"
    Then the converted value is 1.0

  Scenario: Convert kilometers to meters
    Given I have a value of 1.0 in "kilometers"
    When I convert to "meters"
    Then the converted value is 1000.0

  Scenario: Convert Celsius to Fahrenheit
    Given I have a value of 100.0 in "celsius"
    When I convert to "fahrenheit"
    Then the converted value is 212.0

  Scenario: Convert Fahrenheit to Celsius
    Given I have a value of 32.0 in "fahrenheit"
    When I convert to "celsius"
    Then the converted value is 0.0

  Scenario: Convert kilograms to grams
    Given I have a value of 1.0 in "kilograms"
    When I convert to "grams"
    Then the converted value is 1000.0

  Scenario: Convert square meters to square centimeters
    Given I have a value of 1.0 in "square_meter"
    When I convert to "square_centimeter"
    Then the converted value is 10000.0

  Scenario: Convert square feet to square yards
    Given I have a value of 9.0 in "square_foot"
    When I convert to "square_yard"
    Then the converted value is 1.0

  Scenario: Convert liters to milliliters
    Given I have a value of 1.0 in "liter"
    When I convert to "milliliter"
    Then the converted value is 1000.0

  Scenario: Convert gallons to liters
    Given I have a value of 1.0 in "gallon"
    When I convert to "liter"
    Then the converted value is 3.785411784

  Scenario: Convert kilometers per hour to meters per second
    Given I have a value of 36.0 in "kilometer_per_hour"
    When I convert to "meter_per_second"
    Then the converted value is 10.0

  Scenario: Convert knots to kilometers per hour
    Given I have a value of 1.0 in "knot"
    When I convert to "kilometer_per_hour"
    Then the converted value is 1.852

  Scenario: Convert unsupported unit
    Given I have a value of 1.0 in "unknown"
    When I convert to "meters"
    Then there is a conversion error

  Scenario: Convert incompatible units
    Given I have a value of 1.0 in "meters"
    When I convert to "grams"
    Then there is a conversion error