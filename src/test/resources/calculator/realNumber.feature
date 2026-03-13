Feature: Real Number Arithmetic
  Background:
    Given I initialise a calculator

  Scenario: Adding two real numbers
    Given a real number 0.33
    And a real number 3.14
    When I add the real numbers
    Then the real result is 3.47

  Scenario: Subtracting two real numbers
    Given a real number 3.14
    And a real number 0.33
    When I subtract the real numbers
    Then the real result is 2.81

  Scenario: Multiplying two real numbers
    Given a real number 0.33
    And a real number 3.14
    When I multiply the real numbers
    Then the real result is 1.0362

  Scenario: Dividing two real numbers
    Given a real number 3.14
    And a real number 0.33
    When I divide the real numbers
    Then the real result is 9.515151515151516

  Scenario: Division 0.0 by 0.0 gives NaN
    Given a real number 0.0
    And a real number 0.0
    When I divide the real numbers
    Then the real result is NaN

  Scenario: Division 1.0 by 0.0 gives Infinity
    Given a real number 1.0
    And a real number 0.0
    When I divide the real numbers
    Then the real result is Infinity

  Scenario: Division -1.0 by 0.0 gives -Infinity
    Given a real number -1.0
    And a real number 0.0
    When I divide the real numbers
    Then the real result is -Infinity

  Scenario: Square root of negative real number gives NaN
    Given a real number -1.0
    When I take the square root of the real number
    Then the real result is NaN