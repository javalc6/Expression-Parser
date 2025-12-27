/**
 * Test suite for ExpressionParser
 */
package math;
import java.util.ArrayList;
import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestSuite {
    private static final String[] FUNCTIONS = {"sin", "cos", "tan", "log", "exp", "sqrt"};
    private static final String[] OPERATORS = {"+", "-", "*", "/"};
    private static final Random random = new Random();
	private static final double epsilon = 1e-5;

	public static void main(String[] args) {//demo part of the ExpressionParser class
		ExpressionParser ep = new ExpressionParser();
		try {
//-- Manual positive tests
			Node p1 = ep.parseExpression("log(exp((sin(PI/4)+cos(PI/4))/(sqrt(2)*tan(PI/4))))");
			System.out.println(ep.visit(p1));	
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p1) + " expected result: 1.0");
			System.out.println();

			Node p2 = ep.parseExpression("(0 == 0) and (0 != 1) and !false or ((true == false) and (2*2 < 5))");
			System.out.println(ep.visit(p2));
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p2) + " expected result: true");
			System.out.println();

			Node p3 = ep.parseExpression("5 > 4 ? 1 + 2 + 3 : 2 * 2");
			System.out.println(ep.visit(p3));
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p3) + " expected result: 6.0");
			System.out.println();

			ep.registerFunction("cube", x -> x * x * x);
			ep.registerFunction("atan", Math::atan);
			Node p4 = ep.parseExpression("cube(2)*atan(tan(PI/8))/PI");
			System.out.println(ep.visit(p4));
			System.out.println();
			System.out.println("Result: " + ep.evaluate(p4) + " expected result: 1.0");
			System.out.println();

//-- Automatic positive tests
//Note: automatic positive tests may fail due to high sensitivity of the trigonometric functions when dealing with large arguments
			System.out.println("Automatic positive tests");
			int testsPassed = 0;
			int testsFailed = 0;
			// Retrieve a JavaScript engine to use as reference
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			String regex = "\\b(sin|cos|tan|log|exp|sqrt)\\b";//regex to adapt function format to javascript engine

			for (int i = 0; i < 100; i++) {
				int M = random.nextInt(6);
				int P = random.nextInt(6);
				int F = random.nextInt(5);
				int N = random.nextInt(9);
				//generate random expression with correct syntax
				String expr = generateExpression(M, P, F, N);
				Node p = ep.parseExpression(expr);
				double parserResult = (double)ep.evaluate(p);

                double referenceResult = (double)engine.eval(expr.replaceAll(regex, "Math.$1"));

                if (Math.abs(parserResult - referenceResult) > epsilon * Math.abs(referenceResult)) {
                    System.out.println("Test failed, expression: " + expr);
                    System.out.println("Parser result: " + parserResult);
                    System.out.println("Reference result: " + referenceResult);
                    System.out.println("Difference: " + Math.abs(parserResult - referenceResult));
					testsFailed++;
                } else testsPassed++;
			}
			System.out.println();
			System.out.println("testsPassed: " + testsPassed + ", testsFailed: " + testsFailed);
			if (testsFailed > 0) {
				System.out.println();
				System.out.println("Warning: tests may fail due to high sensitivity of the trigonometric functions when dealing with large arguments");
				System.out.println("expressions like sin((sin((exp(95.172) + 457.9)))) requires high-precision arithmetic");
			}
			System.out.println();
		} catch (Exception ex) {
			System.err.println("Exception: " + ex.getMessage());//ex.printStackTrace();
		}

//-- Automatic negative tests
		System.out.println("Automatic negative tests");
		int testsPassed = 0;
		int testsFailed = 0;
		for (int i = 0; i < 100; i++) {
			int M = random.nextInt(6);
			int P = random.nextInt(6);
			int F = random.nextInt(5);
			int N = random.nextInt(9);
			//generate random expression with incorrect syntax
			String expr = buildWrongExpression(M, P, F, N);
			try	{
				Node p = ep.parseExpression(expr);
				double parserResult = (double)ep.evaluate(p);
				testsFailed++;
				System.out.println("Test failed, expression: " + expr);
			} catch (Exception ex) {
				testsPassed++;
			}
		}
		System.out.println();
		System.out.println("testsPassed: " + testsPassed + ", testsFailed: " + testsFailed);
	}

    /**
     * Generates a random valid math expression string.
     */
    private static String generateExpression(int M, int P, int F, int N) {
        return buildExpression(M, P, F, N);
    }

    private static String buildExpression(int m, int p, int f, int n) {
        ArrayList<Integer> actions = new ArrayList<>();
        if (f > 0) actions.add(0); // Add a function
        if (m > 0) actions.add(1); // Add a parenthesis
        if (p > 0 || n > 1) actions.add(2); // Add an operator (splits expression)
        if (actions.isEmpty()) return randomDecimal();

        // Pick a random action from the available pool
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

    private static String randomDecimal() {
        return random.nextInt(1000) + "." + random.nextInt(1000);
    }

    private static String randomWrongDecimal() {
        return random.nextInt(1000) + "a" + random.nextInt(1000);
    }

    /**
     * Generates a random incorrect math expression string. Similar to buildExpression() but with syntax errors.
     */
	 
    private static final String[] OPERATORS_ONLY_BINARY = {"*", "/"};
    private static String buildWrongExpression(int m, int p, int f, int n) {
        ArrayList<Integer> actions = new ArrayList<>();
        if (f > 0) actions.add(0); // Add a function
        if (m > 0) actions.add(1); // Add a parenthesis
        if (p > 0 || n > 1) actions.add(2); // Add an operator (splits expression)
        if (actions.isEmpty()) return randomWrongDecimal();

        // Pick a random action from the available pool
        int choice = actions.get(random.nextInt(actions.size()));
        switch (choice) {
            case 0: // Function
				if (random.nextInt(3) == 0)
	                return "fake(" + buildExpression(m, p, f - 1, n) + ")";
				else return FUNCTIONS[random.nextInt(FUNCTIONS.length)] + "(" + buildWrongExpression(m, p, f - 1, n) + ")";
            case 1: // Parenthesis
				int rnd = random.nextInt(5);
				if (rnd == 0)
	                return buildExpression(m - 1, p, f, n) + ")";
				else if (rnd == 1)
	                return "(" + buildExpression(m - 1, p, f, n);
				else return "(" + buildWrongExpression(m - 1, p, f, n) + ")";
            case 2: // Operator
                int leftF = (f > 0) ? random.nextInt(f + 1) : 0;
                int leftM = (m > 0) ? random.nextInt(m + 1) : 0;
                int leftP = (p > 0) ? random.nextInt(p) : 0;
                int leftN = (n > 1) ? random.nextInt(n) : 1; 
				rnd = random.nextInt(5);
				if (rnd == 0)
	                return "(" + OPERATORS_ONLY_BINARY[random.nextInt(OPERATORS_ONLY_BINARY.length)] +
                       buildExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";            
				else if (rnd == 1)
	                return "(" + buildExpression(leftM, leftP, leftF, leftN) +
                       OPERATORS[random.nextInt(OPERATORS.length)] + ")";            
				else if (rnd == 2)
					return "(" + buildExpression(leftM, leftP, leftF, leftN) +
                       OPERATORS[random.nextInt(OPERATORS.length)] +
                       buildWrongExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";            
				else return "(" + buildWrongExpression(leftM, leftP, leftF, leftN) +
                       OPERATORS[random.nextInt(OPERATORS.length)] +
                       buildExpression(m - leftM, p - 1 - leftP, f - leftF, n - leftN) + ")";            
			default:
                return randomWrongDecimal();
        }
    }

}