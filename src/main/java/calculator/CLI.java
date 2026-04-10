package calculator;

import java.util.Scanner;

/**
 * Command-Line Interface (CLI) for the Calculator.
 * Implements a Read-Eval-Print Loop (REPL) that reads arithmetic expressions
 * from standard input, evaluates them, and prints the result.
 */
public class CLI {

    private final Calculator calculator = new Calculator();

    /**
     * Starts the REPL loop.
     * Reads lines from standard input until "exit" or "quit" is entered.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Calculator REPL!");
        System.out.println("Type an arithmetic expression to evaluate it.");
        System.out.println("Type 'help' for available commands, or 'exit' to quit.");
        System.out.println();

        while (scanner.hasNextLine()) {
            System.out.print("> ");
            System.out.flush();
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.isEmpty()) {
                // Ignore empty lines silently
            } else {
                evaluate(input);
            }
        }

        scanner.close();
    }

    /**
     * Parses and evaluates an arithmetic expression, then prints the result.
     *
     * @param input the expression string entered by the user
     */
    void evaluate(String input) {
        try {
            Expression expr = calculator.read(input);
            NumberValue result = calculator.eval(expr);
            System.out.println("= " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Prints the help message listing available commands and supported operators.
     */
    void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help       - Show this help message");
        System.out.println("  exit       - Exit the calculator");
        System.out.println("  quit       - Exit the calculator");
        System.out.println();
        System.out.println("Supported operators: +, -, *, /");
        System.out.println("Parentheses are supported: (3 + 4) * 2");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  3 + 4");
        System.out.println("  (5 * 2) - 3");
        System.out.println("  10 / (2 + 3)");
    }
}