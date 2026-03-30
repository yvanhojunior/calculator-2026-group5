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

  Scenario: Convert unsupported unit
    Given I have a value of 1.0 in "unknown"
    When I convert to "meters"
    Then there is a conversion error