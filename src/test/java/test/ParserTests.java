package test;

import math.ExpressionParser;
import math.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Targeted unit tests for ExpressionParser covering all operators,
 * functions, and edge cases.
 */
public class ParserTests {
    private static final double EPSILON = 1e-9;
    private ExpressionParser parser;

    @BeforeEach
    void setUp() {
        parser = new ExpressionParser();
        parser.registerFunction("cube", x -> x * x * x);
        parser.registerFunction("atan", Math::atan);
    }

    private double evalDouble(String expression) throws ParseException {
        Node node = parser.parseExpression(expression);
        Object result = parser.evaluate(node);
        assertInstanceOf(Double.class, result, "Expected Double result for: " + expression);
        return (Double) result;
    }

    private boolean evalBoolean(String expression) throws ParseException {
        Node node = parser.parseExpression(expression);
        Object result = parser.evaluate(node);
        assertInstanceOf(Boolean.class, result, "Expected Boolean result for: " + expression);
        return (Boolean) result;
    }

    private String visit(String expression) throws ParseException {
        Node node = parser.parseExpression(expression);
        return parser.visit(node);
    }

    @Nested
    class NumericLiterals {
        @Test
        void integerLiteral() throws ParseException {
            assertEquals(42.0, evalDouble("42"), EPSILON);
        }

        @Test
        void decimalLiteral() throws ParseException {
            assertEquals(3.14159, evalDouble("3.14159"), EPSILON);
        }

        @Test
        void leadingDecimalPoint() throws ParseException {
            assertEquals(0.5, evalDouble(".5"), EPSILON);
        }

        @Test
        void trailingDecimalPoint() throws ParseException {
            assertEquals(5.0, evalDouble("5."), EPSILON);
        }

        @Test
        void zero() throws ParseException {
            assertEquals(0.0, evalDouble("0"), EPSILON);
        }
    }

    @Nested
    class Constants {
        @Test
        void pi() throws ParseException {
            assertEquals(Math.PI, evalDouble("PI"), EPSILON);
        }

        @Test
        void e() throws ParseException {
            assertEquals(Math.E, evalDouble("E"), EPSILON);
        }
    }

    @Nested
    class ArithmeticOperators {
        @Test
        void addition() throws ParseException {
            assertEquals(5.0, evalDouble("2 + 3"), EPSILON);
        }

        @Test
        void subtraction() throws ParseException {
            assertEquals(7.0, evalDouble("10 - 3"), EPSILON);
        }

        @Test
        void multiplication() throws ParseException {
            assertEquals(12.0, evalDouble("3 * 4"), EPSILON);
        }

        @Test
        void division() throws ParseException {
            assertEquals(2.5, evalDouble("5 / 2"), EPSILON);
        }

        @Test
        void unaryMinus() throws ParseException {
            assertEquals(-5.0, evalDouble("-5"), EPSILON);
        }

        @Test
        void unaryMinusOnExpression() throws ParseException {
            assertEquals(-7.0, evalDouble("-(3 + 4)"), EPSILON);
        }

        @Test
        void doubleNegative() throws ParseException {
            assertEquals(5.0, evalDouble("--5"), EPSILON);
        }

        @Test
        void operatorPrecedence() throws ParseException {
            assertEquals(14.0, evalDouble("2 + 3 * 4"), EPSILON);
            assertEquals(20.0, evalDouble("(2 + 3) * 4"), EPSILON);
        }

        @Test
        void leftAssociativity() throws ParseException {
            assertEquals(1.0, evalDouble("10 - 5 - 4"), EPSILON);
            assertEquals(2.0, evalDouble("16 / 4 / 2"), EPSILON);
        }

        @Test
        void divisionByZero() throws ParseException {
            double result = evalDouble("1 / 0");
            assertTrue(Double.isInfinite(result));
        }
    }

    @Nested
    class BooleanLiterals {
        @Test
        void trueLiteral() throws ParseException {
            assertTrue(evalBoolean("true"));
        }

        @Test
        void falseLiteral() throws ParseException {
            assertFalse(evalBoolean("false"));
        }
    }

    @Nested
    class LogicalOperators {
        @Test
        void andTrueTrue() throws ParseException {
            assertTrue(evalBoolean("true and true"));
        }

        @Test
        void andTrueFalse() throws ParseException {
            assertFalse(evalBoolean("true and false"));
        }

        @Test
        void andFalseTrue() throws ParseException {
            assertFalse(evalBoolean("false and true"));
        }

        @Test
        void andFalseFalse() throws ParseException {
            assertFalse(evalBoolean("false and false"));
        }

        @Test
        void orTrueTrue() throws ParseException {
            assertTrue(evalBoolean("true or true"));
        }

        @Test
        void orTrueFalse() throws ParseException {
            assertTrue(evalBoolean("true or false"));
        }

        @Test
        void orFalseTrue() throws ParseException {
            assertTrue(evalBoolean("false or true"));
        }

        @Test
        void orFalseFalse() throws ParseException {
            assertFalse(evalBoolean("false or false"));
        }

        @Test
        void notTrue() throws ParseException {
            assertFalse(evalBoolean("!true"));
        }

        @Test
        void notFalse() throws ParseException {
            assertTrue(evalBoolean("!false"));
        }

        @Test
        void doubleNot() throws ParseException {
            assertTrue(evalBoolean("!!true"));
        }

        @Test
        void logicalPrecedence() throws ParseException {
            // 'and' has higher precedence than 'or'
            assertTrue(evalBoolean("true or false and false"));  // true or (false and false) = true
            assertFalse(evalBoolean("(true or false) and false")); // true and false = false
        }
    }

    @Nested
    class EqualityOperators {
        @Test
        void equalNumbers() throws ParseException {
            assertTrue(evalBoolean("5 == 5"));
        }

        @Test
        void unequalNumbers() throws ParseException {
            assertFalse(evalBoolean("5 == 6"));
        }

        @Test
        void notEqualNumbers() throws ParseException {
            assertTrue(evalBoolean("5 != 6"));
        }

        @Test
        void notEqualSameNumbers() throws ParseException {
            assertFalse(evalBoolean("5 != 5"));
        }

        @Test
        void equalBooleans() throws ParseException {
            assertTrue(evalBoolean("true == true"));
            assertTrue(evalBoolean("false == false"));
        }

        @Test
        void unequalBooleans() throws ParseException {
            assertFalse(evalBoolean("true == false"));
        }
    }

    @Nested
    class RelationalOperators {
        @Test
        void lessThan() throws ParseException {
            assertTrue(evalBoolean("3 < 5"));
            assertFalse(evalBoolean("5 < 3"));
            assertFalse(evalBoolean("5 < 5"));
        }

        @Test
        void lessThanOrEqual() throws ParseException {
            assertTrue(evalBoolean("3 <= 5"));
            assertTrue(evalBoolean("5 <= 5"));
            assertFalse(evalBoolean("5 <= 3"));
        }

        @Test
        void greaterThan() throws ParseException {
            assertTrue(evalBoolean("5 > 3"));
            assertFalse(evalBoolean("3 > 5"));
            assertFalse(evalBoolean("5 > 5"));
        }

        @Test
        void greaterThanOrEqual() throws ParseException {
            assertTrue(evalBoolean("5 >= 3"));
            assertTrue(evalBoolean("5 >= 5"));
            assertFalse(evalBoolean("3 >= 5"));
        }
    }

    @Nested
    class TernaryConditional {
        @Test
        void trueCondition() throws ParseException {
            assertEquals(1.0, evalDouble("true ? 1 : 2"), EPSILON);
        }

        @Test
        void falseCondition() throws ParseException {
            assertEquals(2.0, evalDouble("false ? 1 : 2"), EPSILON);
        }

        @Test
        void expressionCondition() throws ParseException {
            assertEquals(6.0, evalDouble("5 > 4 ? 1 + 2 + 3 : 2 * 2"), EPSILON);
        }

        @Test
        void nestedTernary() throws ParseException {
            assertEquals(3.0, evalDouble("true ? (false ? 1 : 3) : 2"), EPSILON);
        }

        @Test
        void ternaryWithBoolean() throws ParseException {
            assertTrue(evalBoolean("1 < 2 ? true : false"));
        }
    }

    @Nested
    class BuiltInFunctions {
        @Test
        void sin() throws ParseException {
            assertEquals(Math.sin(1.0), evalDouble("sin(1)"), EPSILON);
            assertEquals(1.0, evalDouble("sin(PI/2)"), EPSILON);
        }

        @Test
        void cos() throws ParseException {
            assertEquals(Math.cos(1.0), evalDouble("cos(1)"), EPSILON);
            assertEquals(1.0, evalDouble("cos(0)"), EPSILON);
        }

        @Test
        void tan() throws ParseException {
            assertEquals(Math.tan(1.0), evalDouble("tan(1)"), EPSILON);
            assertEquals(1.0, evalDouble("tan(PI/4)"), EPSILON);
        }

        @Test
        void log() throws ParseException {
            assertEquals(Math.log(10.0), evalDouble("log(10)"), EPSILON);
            assertEquals(1.0, evalDouble("log(E)"), EPSILON);
        }

        @Test
        void exp() throws ParseException {
            assertEquals(Math.exp(2.0), evalDouble("exp(2)"), EPSILON);
            assertEquals(Math.E, evalDouble("exp(1)"), EPSILON);
        }

        @Test
        void sqrt() throws ParseException {
            assertEquals(Math.sqrt(16.0), evalDouble("sqrt(16)"), EPSILON);
            assertEquals(4.0, evalDouble("sqrt(16)"), EPSILON);
        }

        @Test
        void nestedFunctions() throws ParseException {
            assertEquals(1.0, evalDouble("log(exp(1))"), EPSILON);
            assertEquals(2.0, evalDouble("sqrt(sqrt(16))"), EPSILON);
        }
    }

    @Nested
    class UserDefinedFunctions {
        @Test
        void cubeFunction() throws ParseException {
            assertEquals(8.0, evalDouble("cube(2)"), EPSILON);
            assertEquals(27.0, evalDouble("cube(3)"), EPSILON);
        }

        @Test
        void atanFunction() throws ParseException {
            assertEquals(Math.atan(1.0), evalDouble("atan(1)"), EPSILON);
        }

        @Test
        void userFunctionInExpression() throws ParseException {
            assertEquals(1.0, evalDouble("cube(2)*atan(tan(PI/8))/PI"), EPSILON);
        }
    }

    @Nested
    class ComplexExpressions {
        @Test
        void trigIdentity() throws ParseException {
            // sin^2(x) + cos^2(x) = 1
            assertEquals(1.0, evalDouble("sin(1)*sin(1) + cos(1)*cos(1)"), EPSILON);
        }

        @Test
        void compoundExpression() throws ParseException {
            assertEquals(1.0, evalDouble("log(exp((sin(PI/4)+cos(PI/4))/(sqrt(2)*tan(PI/4))))"), EPSILON);
        }

        @Test
        void complexBoolean() throws ParseException {
            assertTrue(evalBoolean("(0 == 0) and (0 != 1) and !false or ((true == false) and (2*2 < 5))"));
        }
    }

    @Nested
    class VisitMethod {
        @Test
        void simpleExpression() throws ParseException {
            String result = visit("1 + 2");
            assertEquals("(1.0 + 2.0)", result);
        }

        @Test
        void functionExpression() throws ParseException {
            String result = visit("sin(PI)");
            assertEquals("sin(PI)", result);
        }
    }

    @Nested
    class ParseErrors {
        @Test
        void missingCloseParen() {
            ParseException ex = assertThrows(ParseException.class, () -> parser.parseExpression("(1 + 2"));
            assertTrue(ex.getMessage().contains(")"));
        }

        @Test
        void missingOperand() {
            assertThrows(ParseException.class, () -> parser.parseExpression("1 +"));
        }

        @Test
        void invalidCharacter() {
            assertThrows(ParseException.class, () -> parser.parseExpression("1 @ 2"));
        }

        @Test
        void unknownFunction() {
            assertThrows(Exception.class, () -> parser.parseExpression("unknown(1)"));
        }

        @Test
        void emptyExpression() {
            assertThrows(ParseException.class, () -> parser.parseExpression(""));
        }

        @Test
        void trailingCharacters() {
            assertThrows(ParseException.class, () -> parser.parseExpression("1 + 2 3"));
        }

        @Test
        void invalidEquality() {
            assertThrows(ParseException.class, () -> parser.parseExpression("1 = 2"));
        }

        @Test
        void missingFunctionParen() {
            assertThrows(ParseException.class, () -> parser.parseExpression("sin 1"));
        }
    }

    @Nested
    class Whitespace {
        @Test
        void noWhitespace() throws ParseException {
            assertEquals(5.0, evalDouble("2+3"), EPSILON);
        }

        @Test
        void extraWhitespace() throws ParseException {
            assertEquals(5.0, evalDouble("  2  +  3  "), EPSILON);
        }

        @Test
        void tabsAndSpaces() throws ParseException {
            assertEquals(5.0, evalDouble("\t2\t+\t3\t"), EPSILON);
        }
    }
}
