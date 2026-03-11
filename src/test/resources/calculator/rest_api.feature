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