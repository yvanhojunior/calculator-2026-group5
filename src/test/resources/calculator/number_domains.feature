Feature: Support for different types of numbers

  Background:
    Given I initialise a calculator

  # Real numbers
  Scenario: Evaluating a real number addition
    Given the expression "1.5 + 2.5"
    Then its value is "4.0"

  Scenario: Evaluating a real number multiplication
    Given the expression "2.0 * 3.5"
    Then its value is "7.0"

  Scenario: Evaluating a real number subtraction
    Given the expression "5.0 - 3.0"
    Then its value is "2.0"

  # Rational numbers
  Scenario: Evaluating a rational number addition
    Given the expression "1/3 + 1/6"
    Then its value is "1/2"

  Scenario: Evaluating a rational number multiplication
    Given the expression "2/3 * 3/4"
    Then its value is "1/2"