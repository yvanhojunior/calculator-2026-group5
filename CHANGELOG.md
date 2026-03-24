# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

## [1.0.0] - 2026-03-12

