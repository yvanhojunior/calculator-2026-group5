package calculator;

import calculator.parser.ExpressionParser;
import java.util.Scanner;

public class CLI {
    private final Calculator calculator = new Calculator();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Calculator!");
        System.out.println("Type 'help' for commands, 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (!input.isEmpty()) {
                try {
                    Expression expr = ExpressionParser.parse(input);
                    System.out.println("= " + calculator.eval(expr));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help  - Show this help");
        System.out.println("  exit/quit  - Exit the REPL");
        System.out.println("Supported operators: +, -, *, /");
        System.out.println("Example: 2 * 2");
    }
}