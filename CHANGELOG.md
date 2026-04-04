# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
- Support for multiple categories:
    - Length
    - Weight
    - Temperature
    - Area
    - Volume
    - Speed
- Bidirectional conversion support (input in either field)
- REST API endpoint `/api/convert`

### Improved
- UI navigation with category tabs
- Input handling and validation
- Error handling for unsupported and incompatible units

### Tests
- Added Cucumber scenarios for all supported categories
- Improved tolerance for floating-point comparisons
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

## [1.2.6] - 2026-03-31

### Changed
- `RationalValue.toString()` now displays mixed numbers (e.g. 3/2 → 1 1/2)

### Added
- Tests for mixed number representation

### Closes
- #18 Support for rational numbers

## [1.2.7] - 2026-03-31

### Added
- Cucumber BDD scenarios for complex numbers

### Closes
- #19 Support for complex numbers