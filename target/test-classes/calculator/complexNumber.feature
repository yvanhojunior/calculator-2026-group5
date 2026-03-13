Feature: Complex Number Arithmetic
  Background:
    Given I initialise a calculator

  Scenario: Adding two complex numbers
    Given a complex number "2+3i"
    And a complex number "1+2i"
    When I add the complex numbers
    Then the complex result is "3+5i"

  Scenario: Subtracting two complex numbers
    Given a complex number "2+3i"
    And a complex number "1+2i"
    When I subtract the complex numbers
    Then the complex result is "1+1i"

  Scenario: Multiplying two complex numbers
    Given a complex number "2+3i"
    And a complex number "1+2i"
    When I multiply the complex numbers
    Then the complex result is "-4+7i"

  Scenario: Dividing two complex numbers
    Given a complex number "2+3i"
    And a complex number "1+2i"
    When I divide the complex numbers
    Then the complex result is "1.6-0.2i"

  Scenario: Division by zero with complex numbers
    Given a complex number "2+3i"
    And a complex number "0+0i"
    Then dividing complex numbers throws an ArithmeticException