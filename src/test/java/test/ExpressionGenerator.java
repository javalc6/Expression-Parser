package test;

import java.util.ArrayList;
import java.util.Random;

/**
 * Utility class for generating random mathematical expressions for testing.
 * Generates both valid expressions (for positive tests) and intentionally
 * malformed expressions (for negative tests).
 */
public class ExpressionGenerator {
    private static final String[] FUNCTIONS = {"sin", "cos", "tan", "log", "exp", "sqrt"};
    private static final String[] OPERATORS = {"+", "-", "*", "/"};
    private static final String[] OPERATORS_ONLY_BINARY = {"*", "/"};

    private final Random random;

    public ExpressionGenerator() {
        this.random = new Random();
    }

    public ExpressionGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates a random valid math expression string.
     *
     * @param maxParentheses maximum nesting depth for parentheses
     * @param maxOperators maximum number of binary operators
     * @param maxFunctions maximum number of function calls
     * @param maxNumbers maximum number of numeric literals
     * @return a syntactically valid expression string
     */
    public String generateValidExpression(int maxParentheses, int maxOperators, int maxFunctions, int maxNumbers) {
        return buildExpression(maxParentheses, maxOperators, maxFunctions, maxNumbers);
    }

    /**
     * Generates a random invalid math expression string containing syntax errors.
     *
     * @param maxParentheses maximum nesting depth for parentheses
     * @param maxOperators maximum number of binary operators
     * @param maxFunctions maximum number of function calls
     * @param maxNumbers maximum number of numeric literals
     * @return a syntactically invalid expression string
     */
    public String generateInvalidExpression(int maxParentheses, int maxOperators, int maxFunctions, int maxNumbers) {
        return buildWrongExpression(maxParentheses, maxOperators, maxFunctions, maxNumbers);
    }

    /**
     * Generates random parameters for expression generation.
     *
     * @return array of [maxParentheses, maxOperators, maxFunctions, maxNumbers]
     */
    public int[] randomParameters() {
        return new int[] {
            random.nextInt(6),  // M - parentheses
            random.nextInt(6),  // P - operators
            random.nextInt(5),  // F - functions
            random.nextInt(9)   // N - numbers
        };
    }

    private String buildExpression(int m, int p, int f, int n) {
        ArrayList<Integer> actions = new ArrayList<>();
        if (f > 0) actions.add(0); // Add a function
        if (m > 0) actions.add(1); // Add a parenthesis
        if (p > 0 || n > 1) actions.add(2); // Add an operator (splits expression)
        if (actions.isEmpty()) return randomDecimal();

        int choice = actions.get(random.nextInt(actions.size()));
        switch (choice) {
            case 0: // Function
                return FUNCTIONS[random.nextInt(FUNCTIONS.length)] + "(" + buildExpression(m, p, f - 1, n) + ")";
            case 1: // Parenthesis
                return "(" + buildExpression(m - 1, p, f, n) + ")";
            case 2: // Operator
                int leftF = (f > 0) ? random.nextInt(f + 1) : 0;
                int leftM = (m > 0) ? random.nextInt(m + 1) : 0;
                int leftP = (p > 0) ? random.nextInt(p) : 0;
                int leftN = (n > 1) ? random.nextInt(n) : 1;
                return "(" + buildExpression(leftM, leftP, leftF, leftN) +
                       OPERATORS[random.nextInt(OPERATORS.length)] +
                       buildExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";
            default:
                return randomDecimal();
        }
    }

    private String buildWrongExpression(int m, int p, int f, int n) {
        ArrayList<Integer> actions = new ArrayList<>();
        if (f > 0) actions.add(0); // Add a function
        if (m > 0) actions.add(1); // Add a parenthesis
        if (p > 0 || n > 1) actions.add(2); // Add an operator (splits expression)
        if (actions.isEmpty()) return randomWrongDecimal();

        int choice = actions.get(random.nextInt(actions.size()));
        switch (choice) {
            case 0: // Function - sometimes use fake function name
                if (random.nextInt(3) == 0) {
                    return "fake(" + buildExpression(m, p, f - 1, n) + ")";
                } else {
                    return FUNCTIONS[random.nextInt(FUNCTIONS.length)] + "(" + buildWrongExpression(m, p, f - 1, n) + ")";
                }
            case 1: // Parenthesis - sometimes mismatched
                int rnd = random.nextInt(5);
                if (rnd == 0) {
                    return buildExpression(m - 1, p, f, n) + ")"; // Missing open paren
                } else if (rnd == 1) {
                    return "(" + buildExpression(m - 1, p, f, n); // Missing close paren
                } else {
                    return "(" + buildWrongExpression(m - 1, p, f, n) + ")";
                }
            case 2: // Operator - sometimes missing operand
                int leftF = (f > 0) ? random.nextInt(f + 1) : 0;
                int leftM = (m > 0) ? random.nextInt(m + 1) : 0;
                int leftP = (p > 0) ? random.nextInt(p) : 0;
                int leftN = (n > 1) ? random.nextInt(n) : 1;
                rnd = random.nextInt(5);
                if (rnd == 0) {
                    // Missing left operand for binary-only operator
                    return "(" + OPERATORS_ONLY_BINARY[random.nextInt(OPERATORS_ONLY_BINARY.length)] +
                           buildExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";
                } else if (rnd == 1) {
                    // Missing right operand
                    return "(" + buildExpression(leftM, leftP, leftF, leftN) +
                           OPERATORS[random.nextInt(OPERATORS.length)] + ")";
                } else if (rnd == 2) {
                    // Right side is wrong
                    return "(" + buildExpression(leftM, leftP, leftF, leftN) +
                           OPERATORS[random.nextInt(OPERATORS.length)] +
                           buildWrongExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";
                } else {
                    // Left side is wrong
                    return "(" + buildWrongExpression(leftM, leftP, leftF, leftN) +
                           OPERATORS[random.nextInt(OPERATORS.length)] +
                           buildExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";
                }
            default:
                return randomWrongDecimal();
        }
    }

    private String randomDecimal() {
        return random.nextInt(1000) + "." + random.nextInt(1000);
    }

    private String randomWrongDecimal() {
        return random.nextInt(1000) + "a" + random.nextInt(1000);
    }
}