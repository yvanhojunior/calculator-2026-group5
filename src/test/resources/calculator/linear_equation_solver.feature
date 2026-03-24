Feature: Linear Equation Solver

  Scenario: Solving a simple equation with one variable
    Given the linear equation "2x + 3 = 7"
    When I solve the equation
    Then the solution is "x = 2.0"

  Scenario: Solving an equation with no solution
    Given the linear equation "0x + 3 = 7"
    When I solve the equation
    Then there is no solution

  Scenario: Solving an equation with infinite solutions
    Given the linear equation "0x + 0 = 0"
    When I solve the equation
    Then there are infinite solutions

  Scenario: Solving an equation with a syntactic error
    Given the linear equation "2x + = 7"
    When I solve the equation
    Then there is a syntax error

  Scenario: Solving a system of two equations
    Given the linear equations:
      | 2x + y = 5 |
      | x - y = 1  |
    When I solve the system
    Then the solution is "x = 2.0, y = 1.0"