# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.3] - 2026-03-31

### Fixed
- Fixed GitHub Actions release-report workflow permissions.
- Added the required permission to publish Surefire reports in the workflow UI.
- Fixed JavaDoc generation in the release-report workflow.
- Ensured required report artifacts are generated before validation and upload.

## [1.2.2] - 2026-03-31

### Fixed
- Fixed the GitHub Actions release-report workflow permissions.
- Added the required `checks: write` permission so the Surefire test report can be published in the GitHub Actions UI.
- Fixed release report publication for published GitHub releases.

## [1.2.0] - 2026-03-31

### Added
- `LinearEquationSolver` class to solve single linear equations (e.g. `2x + 3 = 7`)
- `LinearEquationSolver.solveSystem()` to solve systems of two equations (e.g. `2x + y = 5`, `x - y = 1`)
- `SolutionType` enum with `UNIQUE`, `NO_SOLUTION`, `INFINITE_SOLUTIONS`, `SYNTAX_ERROR` states
- REST API endpoint `POST /api/solve` to solve a single equation
- REST API endpoint `POST /api/solve/system` to solve a system of two equations
- Visual interface with a dedicated "Linear Equations" tab in the calculator UI
- Cucumber BDD scenarios covering all solution cases

### Changed
- Calculator UI redesigned with a sidebar navigation layout
- Split UI logic into separate JS files (`main.js`, `equations.js`)

## [1.1.0] - 2026-03-24

### Added
- `NumberValue` interface to abstract number representation across all domains
- `IntegerValue` class for integer arithmetic
- `RealValue` class for real number arithmetic (supports NaN, +Infinity, -Infinity)
- `RationalValue` class for rational number arithmetic (fractions p/q, auto-reduced)
- `ComplexValue` class for complex number arithmetic (a + bi)
- Extended ANTLR grammar to parse real, rational and complex numbers
- JUnit tests for all new number domains
- Cucumber BDD scenarios for real and rational numbers

### Changed
- `MyNumber` now wraps a `NumberValue` instead of a primitive `int`
- `Evaluator` now returns `NumberValue` instead of `int`
- `Calculator.eval()` now returns `NumberValue` instead of `int`
- `Operation.op()` now works with `NumberValue` instead of `int`

## [1.2.1] - 2026-03-31

### Changed
- Replaced `double` with `BigDecimal` in `RealValue` for arbitrary precision
- Added configurable precision via `MathContext` (default: 10 significant digits)
- Added special constants: `NaN`, `+Infinity`, `-Infinity`, `Pi`, `E`
- Proper handling of special cases in all arithmetic operations
- Fixed `toString()` to always display decimal point for real numbers

### Closes
- #17 Support for real numbers


## [1.0.0] - 2026-03-12