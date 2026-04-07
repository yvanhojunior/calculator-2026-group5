# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## [1.2.14] - 2026-04-07
### Refactored
- Split `CalculatorController` into separate controllers for better separation of concerns:
    - `CalculateController` → `GET /api/calculate`
    - `SolveController` → `POST /api/solve` and `POST /api/solve/system`
    - `ConvertController` → `POST /api/convert`
    - `CurrencyController` → `GET /api/currencies`
### Closes
- #8 REST API

## [1.2.13] - 2026-04-07
### Fixed
- `RationalValue`: added `IntegerValue` support in `add`, `sub`, `mul`, `div` operations via `toRational()` helper
- Mixed type arithmetic between `RationalValue` and `IntegerValue` now works correctly (e.g. `(1÷3)×9` → `3`)
## [1.2.12] - 2026-04-05
### Added
- Support for exponentiation operator `^` (e.g. `2^3` → `8`)
- Support for negative numbers in expressions (e.g. `-4 + 2` → `-2`)
- `Power` class for exponentiation operation
- `pow()` method in `NumberValue` interface, implemented in `IntegerValue`, `RealValue`, `RationalValue`, `ComplexValue`
### Fixed
- `IntegerValue`: added `RationalValue` support in `add`, `sub`, `mul` operations
- `IntegerValue.pow()`: handles negative exponents and `RationalValue` exponents
- `RealValue.pow()`: handles `RationalValue` exponents
- `CalculatorController`: use ANTLR parser (`calculator.read()`) instead of manual parser
- `CalculatorController`: decode `%5E` → `^` in URL
- `ExpressionAstBuilder`: handle unary minus via `0 - atom`
### Closes
- #100 Support for exponentiation operator (^)
- #101 Support for negative numbers in expressions

## [1.2.12] - 2026-04-05
### Added
- Support for exponentiation operator `^` (e.g. `2^3` → `8`)
- Support for negative numbers in expressions (e.g. `-4 + 2` → `-2`)
- `Power` class for exponentiation operation
- `pow()` method in `NumberValue` interface, implemented in all number domains
### Fixed
- `IntegerValue`: added `RationalValue` support in `add`, `sub`, `mul` operations
- `IntegerValue.pow()`: handles negative and `RationalValue` exponents
- `RealValue.pow()`: handles `RationalValue` exponents
- `CalculatorController`: use ANTLR parser instead of manual parser
- `CalculatorController`: decode `%5E` → `^` in URL
### Closes
- #100 Support for exponentiation operator (^)
- #101 Support for negative numbers in expressions

## [1.2.11] - 2026-04-04
### Fixed
- Updated tests to reflect new real number division behavior
- Unified Cucumber step `{double}` to handle both integer and real results
- Fixed `TestEvaluator` division case to expect `RealValue` when division is not exact

## [1.2.10] - 2026-04-04
### Added
- Real number support in the calculator frontend and backend
### Fixed
- `app.js`: regex updated to accept decimal input, `parseInt` replaced by `parseFloat`
- `ExpressionParser`: detects `.` in number strings and creates `RealValue` accordingly
- `RealValue`: added `toReal()` helper to convert `IntegerValue` before arithmetic operations
- `IntegerValue`: mixed operations with `RealValue` now return `RealValue`
- `IntegerValue.div`: returns `RealValue` when division is not exact (e.g. `7/5` → `1.4`)
- `RealValue`: use `BigDecimal.valueOf` instead of `new BigDecimal` for better precision
### Closes
- #17 Support for real numbers

## [1.2.9] - 2026-04-01
### Added
- Unit Converter feature integrated into calculator UI
- Support for multiple categories: Length, Weight, Temperature, Area, Volume, Speed
- Bidirectional conversion support
- REST API endpoint `/api/convert`
### Improved
- UI navigation with category tabs
- Input handling and validation
- Error handling for unsupported and incompatible units
### Tests
- Added Cucumber scenarios for all supported categories
- Improved tolerance for floating-point comparisons

## [1.2.7] - 2026-03-31
### Added
- Cucumber BDD scenarios for complex numbers
### Closes
- #19 Support for complex numbers

## [1.2.6] - 2026-03-31
### Changed
- `RationalValue.toString()` now displays mixed numbers (e.g. 3/2 → 1 1/2)
### Added
- Tests for mixed number representation
### Closes
- #18 Support for rational numbers

## [1.2.3] - 2026-03-31
### Fixed
- Fixed GitHub Actions release-report workflow permissions
- Added the required permission to publish Surefire reports in the workflow UI
- Fixed JavaDoc generation in the release-report workflow
- Ensured required report artifacts are generated before validation and upload

## [1.2.2] - 2026-03-31
### Fixed
- Fixed the GitHub Actions release-report workflow permissions
- Added the required `checks: write` permission
- Fixed release report publication for published GitHub releases

## [1.2.1] - 2026-03-31
### Changed
- Replaced `double` with `BigDecimal` in `RealValue` for arbitrary precision
- Added configurable precision via `MathContext` (default: 10 significant digits)
- Added special constants: `NaN`, `+Infinity`, `-Infinity`, `Pi`, `E`
- Proper handling of special cases in all arithmetic operations
- Fixed `toString()` to always display decimal point for real numbers
### Closes
- #17 Support for real numbers

## [1.2.0] - 2026-03-31
### Added
- `LinearEquationSolver` class to solve single linear equations
- `LinearEquationSolver.solveSystem()` to solve systems of two equations
- `SolutionType` enum with `UNIQUE`, `NO_SOLUTION`, `INFINITE_SOLUTIONS`, `SYNTAX_ERROR` states
- REST API endpoints `POST /api/solve` and `POST /api/solve/system`
- Visual interface with a dedicated "Linear Equations" tab
- Cucumber BDD scenarios covering all solution cases
### Changed
- Calculator UI redesigned with a sidebar navigation layout
- Split UI logic into separate JS files (`main.js`, `equations.js`)

## [1.1.0] - 2026-03-24
### Added
- `NumberValue` interface to abstract number representation across all domains
- `IntegerValue`, `RealValue`, `RationalValue`, `ComplexValue` classes
- Extended ANTLR grammar to parse real, rational and complex numbers
- JUnit tests for all new number domains
- Cucumber BDD scenarios for real and rational numbers
### Changed
- `MyNumber` now wraps a `NumberValue` instead of a primitive `int`
- `Evaluator` now returns `NumberValue` instead of `int`
- `Calculator.eval()` now returns `NumberValue` instead of `int`
- `Operation.op()` now works with `NumberValue` instead of `int`