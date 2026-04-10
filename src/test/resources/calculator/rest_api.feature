Feature: Calculator REST API
  This feature provides a range of scenarios corresponding to the
  intended external behaviour of the calculator REST API.

  Background:
    Given the REST API server is running on port 8080

  Scenario: Evaluating a simple addition via REST API
    When I send a GET request to "/api/calculate" with expression "3+4"
    Then the API returns status code 200
    And the response contains result 7

  Scenario: Evaluating a subtraction via REST API
    When I send a GET request to "/api/calculate" with expression "10-3"
    Then the API returns status code 200
    And the response contains result 7

  Scenario: Evaluating a multiplication via REST API
    When I send a GET request to "/api/calculate" with expression "3*4"
    Then the API returns status code 200
    And the response contains result 12

  Scenario: Evaluating a division via REST API
    When I send a GET request to "/api/calculate" with expression "10/2"
    Then the API returns status code 200
    And the response contains result 5

  Scenario: Division by zero via REST API
    When I send a GET request to "/api/calculate" with expression "5/0"
    Then the API returns status code 400
    And the response contains an error message

  Scenario: Invalid expression via REST API
    When I send a GET request to "/api/calculate" with expression "abc"
    Then the API returns status code 400
    And the response contains an error message

  Scenario Outline: Evaluating arithmetic operations via REST API
    When I send a GET request to "/api/calculate" with expression "<expression>"
    Then the API returns status code 200
    And the response contains result <result>

    Examples:
      | expression | result |
      | 1+1        | 2      |
      | 10-5       | 5      |
      | 3*3        | 9      |
      | 8/2        | 4      |

  Scenario: Evaluating a wrong expression via REST API
  When I send a GET request to "/api/calculate" with expression "3+"
  Then the API returns status code 400

  Scenario: Evaluating a complex expression via REST API
    When I send a GET request to "/api/calculate" with expression "2*(3+4)"
    Then the API returns status code 200
    And the response contains result 14

  Scenario: Evaluating an expression with whitespace via REST API
    When I send a GET request to "/api/calculate" with expression " 3 + 4 "
    Then the API returns status code 200
    And the response contains result 7

  Scenario: Evaluating an expression with parentheses via REST API
    When I send a GET request to "/api/calculate" with expression "(2+3)*4"
    Then the API returns status code 200
    And the response contains result 20

  Scenario: Evaluating an expression with multiple operators via REST API
    When I send a GET request to "/api/calculate" with expression "2+3*4"
    Then the API returns status code 200
    And the response contains result 14

  Scenario: Evaluating an expression with negative numbers via REST API
    When I send a GET request to "/api/calculate" with expression "-3+5"
    Then the API returns status code 200
    And the response contains result 2

  Scenario: Evaluating an expression with exponentiation via REST API
    When I send a GET request to "/api/calculate" with expression "2^3"
    Then the API returns status code 200
    And the response contains result 8

  Scenario: Evaluating an expression with modulus operator via REST API
    When I send a GET request to "/api/calculate" with expression "10%3"
    Then the API returns status code 200
    And the response contains result 1

  Scenario: Evaluating an expression with multiple parentheses via REST API
    When I send a GET request to "/api/calculate" with expression "((2+3)*4)-5"
    Then the API returns status code 200
    And the response contains result 15

  Scenario: Evaluating an expression with nested parentheses via REST API
    When I send a GET request to "/api/calculate" with expression "2*(3+(4*5))"
    Then the API returns status code 200
    And the response contains result 46

  Scenario: Evaluating an expression with division by zero via REST API
    When I send a GET request to "/api/calculate" with expression "10/0"
    Then the API returns status code 400
    And the response contains an error message