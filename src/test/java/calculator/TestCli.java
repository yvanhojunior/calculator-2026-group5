package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CLI} class.
 * Tests cover the REPL loop, help command, expression evaluation and error handling.
 *
 */
class TestCli {

    private CLI cli;
    private ByteArrayOutputStream outputStream;

    /**
     * Sets up a fresh CLI instance and output capture before each test.
     */
    @BeforeEach
    void setUp() {
        cli = new CLI();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Helper method to get the captured output as a String.
     *
     * @return the captured standard output
     */
    private String getOutput() {
        return outputStream.toString();
    }

    /**
     * Tests that the help command prints the available commands.
     */
    @Test
    void testHelpCommand() {
        cli.printHelp();
        String output = getOutput();
        assertTrue(output.contains("Available commands"), "Help should list available commands");
        assertTrue(output.contains("exit"), "Help should mention exit command");
        assertTrue(output.contains("quit"), "Help should mention quit command");
        assertTrue(output.contains("help"), "Help should mention help command");
    }

    /**
     * Tests that a valid addition expression is correctly evaluated.
     */
    @Test
    void testValidAddition() {
        cli.evaluate("3 + 4");
        assertTrue(getOutput().contains("7"), "3 + 4 should equal 7");
    }

    /**
     * Tests that a valid subtraction expression is correctly evaluated.
     */
    @Test
    void testValidSubtraction() {
        cli.evaluate("10 - 3");
        assertTrue(getOutput().contains("7"), "10 - 3 should equal 7");
    }

    /**
     * Tests that a valid multiplication expression is correctly evaluated.
     */
    @Test
    void testValidMultiplication() {
        cli.evaluate("3 * 4");
        assertTrue(getOutput().contains("12"), "3 * 4 should equal 12");
    }

    /**
     * Tests that a valid division expression is correctly evaluated.
     */
    @Test
    void testValidDivision() {
        cli.evaluate("10 / 2");
        assertTrue(getOutput().contains("5"), "10 / 2 should equal 5");
    }

    /**
     * Tests that an expression with parentheses is correctly evaluated.
     */
    @Test
    void testExpressionWithParentheses() {
        cli.evaluate("(3 + 4) * 2");
        assertTrue(getOutput().contains("14"), "(3 + 4) * 2 should equal 14");
    }

    /**
     * Tests that an invalid expression prints an error message.
     */
    @Test
    void testInvalidExpression() {
        cli.evaluate("abc");
        assertTrue(getOutput().contains("Error"), "Invalid expression should print an error");
    }

    /**
     * Tests that division by zero prints an error message.
     */
    @Test
    void testDivisionByZero() {
        cli.evaluate("5 / 0");
        assertTrue(getOutput().contains("Error") || getOutput().contains("NaN") || getOutput().contains("Infinity"),
                "Division by zero should be handled gracefully");
    }

    /**
     * Tests that the REPL terminates when "exit" is entered.
     */
    @Test
    void testExitCommand() {
        String simulatedInput = "exit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cli.start();
        assertTrue(getOutput().contains("Goodbye!"), "Exit command should print Goodbye!");
    }

    /**
     * Tests that the REPL terminates when "quit" is entered.
     */
    @Test
    void testQuitCommand() {
        String simulatedInput = "quit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cli.start();
        assertTrue(getOutput().contains("Goodbye!"), "Quit command should print Goodbye!");
    }

    /**
     * Tests that the REPL evaluates an expression and then exits.
     */
    @Test
    void testReplEvaluatesAndExits() {
        String simulatedInput = "2 + 2\nexit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cli.start();
        String output = getOutput();
        assertTrue(output.contains("4"), "REPL should evaluate 2 + 2 = 4");
        assertTrue(output.contains("Goodbye!"), "REPL should exit cleanly");
    }
}