Feature: Rational Number Arithmetic
  Background:
    Given I initialise a calculator

  Scenario: Adding two rational numbers
    Given a rational number 1/3
    And a rational number 15/2
    When I add them
    Then the result is the rational number 47/6

  Scenario: Subtracting two rational numbers
    Given a rational number 15/2
    And a rational number 1/3
    When I subtract them
    Then the result is the rational number 43/6

  Scenario: Multiplying two rational numbers
    Given a rational number 1/3
    And a rational number 15/2
    When I multiply them
    Then the result is the rational number 15/6

  Scenario: Dividing two rational numbers
    Given a rational number 15/2
    And a rational number 1/3
    When I divide them
    Then the result is the rational number 45/2

  Scenario: Division by zero with rational numbers
    Given a rational number 1/3
    And a rational number 0/1
    Then dividing them throws an ArithmeticException