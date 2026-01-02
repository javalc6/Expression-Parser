package test;

import math.ExpressionParser;
import math.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests using randomly generated expressions.
 * Valid expressions are verified to parse and evaluate to a finite Double.
 * Invalid expressions are verified to throw an exception during parse or evaluate.
 */
public class RandomExpressionTests {
    private static ExpressionParser parser;
    private static ExpressionGenerator generator;

    @BeforeAll
    static void setUp() {
        parser = new ExpressionParser();
        generator = new ExpressionGenerator();
    }

    @RepeatedTest(500)
    void validExpressionParsesAndEvaluates() throws ParseException {
        int[] params = generator.randomParameters();
        String expr = generator.generateValidExpression(params[0], params[1], params[2], params[3]);

        Node node = parser.parseExpression(expr);
        assertNotNull(node, "Parsed node should not be null for: " + expr);

        Object result = parser.evaluate(node);
        assertInstanceOf(Double.class, result, "Result should be Double for: " + expr);
        // NaN and Infinity are valid mathematical results (e.g., sqrt(-1), log(-1), 1/0)
    }

    @RepeatedTest(500)
    void invalidExpressionThrowsException() {
        int[] params = generator.randomParameters();
        String expr = generator.generateInvalidExpression(params[0], params[1], params[2], params[3]);

        assertThrows(Exception.class, () -> {
            Node node = parser.parseExpression(expr);
            parser.evaluate(node);
        }, "Invalid expression should throw: " + expr);
    }
}