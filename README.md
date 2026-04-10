[![](https://img.shields.io/github/v/release/yvanhojunior/calculator-2026-group5?label=Latest%20Release)](https://github.com/yvanhojunior/calculator-2026-group5/releases/latest)

Code quality: ![Maven Build](https://github.com/yvanhojunior/calculator-2026-group5/actions/workflows/maven.yml/badge.svg)

Test coverage: ![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)


# Calculating arithmetic expressions

## About

This repository contains Java code for computing arithmetic expressions. It was developed as part of the Software Evolution course at the University of Mons (UMONS), Belgium, by Group 5.

The project extends a basic Java calculator into a full-fledged client-server web application with a REST API, a web-based GUI, and support for multiple number domains.

**For more information, please read the [Wiki pages](https://github.com/yvanhojunior/calculator-2026-group5/wiki).**

## Features

- **Arithmetic expressions** — supports `+`, `-`, `*`, `/`, `^` (exponentiation)
- **Multiple number domains** — integers, real numbers, rational numbers, complex numbers
- **Negative numbers** — e.g. `-4 + 2`, `2 * -3`
- **REST API** — available endpoints:
  - `GET /api/calculate?expression=...` — evaluate an arithmetic expression (e.g. `2+3`, `2^3`, `-4+2`)
  - `GET /api/currencies` — get list of available currencies
  - `POST /api/convert` — convert between units of measurement
  - `POST /api/solve` — solve a single linear equation (e.g. `2x + 3 = 7`)
  - `POST /api/solve/system` — solve a system of two linear equations
  - `POST /api/switchFormat` — switch number display format
- **Web-based GUI** — scientific calculator interface
- **Linear equation solver** — single equations and systems of two equations
- **Unit converter** — length, weight, temperature, area, volume, speed

### Unit testing and BDD

*  All tests can be found in the `src/test` directory. They serve as executable documentation of the source code.
*  The source code is accompanied by a set of JUnit 5 unit tests. If you are not familiar with unit testing or JUnit 5, please refer to https://junit.org/junit5/.
*  The source code is accompanied by a set of Cucumber BDD scenarios, also running in JUnit. If you are not familiar with Cucumber and BDD, please refer to https://cucumber.io/docs/cucumber/.
*  The BDD scenarios are specified as `.feature` files in the `src/test/resources` directory.

### Prerequisites

*  You will need to have a running version of Java 22 or higher on your machine.
*  You will need to have a running version of Maven, since this project is accompanied by a `pom.xml` file.

### Installation and testing instructions

*  Upon first use, run `mvn clean install` to download all required dependencies.
*  Compile the source code: `mvn compile`
*  Run the application: `mvn spring-boot:run` then open http://localhost:8080
*  Run tests: `mvn test`
*  Skip tests: add `-DskipTests` to any Maven command.
*  For more advanced Maven usage, refer to https://maven.apache.org/guides/.

### Test coverage and JavaDoc reporting

*  Running `mvn test` generates a test coverage report (HTML) using JaCoCo in `target/site/jacoco`.
*  Running `mvn package` generates JavaDoc documentation in `target/site/apidocs`.

## Built With

*  [Maven](https://maven.apache.org/) - build automation and dependency management
*  [Spring Boot](https://spring.io/projects/spring-boot) - REST API backend
*  [JUnit5](https://junit.org/junit5/) - unit testing framework
*  [Cucumber](https://cucumber.io/docs/cucumber/) - Behaviour-Driven Development
*  [ANTLR4](https://www.antlr.org/) - expression parser grammar
*  [JaCoCo](https://www.jacoco.org) - code coverage library
*  [JavaDoc](https://docs.oracle.com/en/java/javase/21/javadoc/javadoc.html) - code documentation

## Versions

We use [SemVer](http://semver.org/) for semantic versioning. For the versions available, see the [tags on this repository](https://github.com/yvanhojunior/calculator-2026-group5/tags).

## Contributors

* Tom Mens — Original project template
* Gauvain Devillez @GauvainD — Original project template
* Yvan — Backend: Expression Parser, Number Domains (Integer, Real, Rational, Complex), Exponentiation operator, Negative numbers support
* Franck — REST API, CLI, Unit Converter, Linear Equation Solver
* Gilles — Web-based GUI

## Licence

[This code is available under the GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/) (GPLv3)

## Acknowledgments

* Software Engineering Lab, Faculty of Sciences, University of Mons, Belgium.