# Expression Parser

A recursive descent expression parser for mathematical and boolean expressions in Java.

## Quick Start

# How to use
Instantiate an ExpressionParser:
```
ExpressionParser ep = new ExpressionParser();
```
Use *ep* to call one of the following methods to parse and evaluate math expressions:
```
parseExpression(String expr): parses the given expression and returns a binary tree representing the parse expression;

ExpressionParser parser = new ExpressionParser();

// Parse and evaluate a numeric expression
Node node = parser.parseExpression("2 + 3 * 4");
Double result = (Double) parser.evaluate(node);  // 14.0

// Parse and evaluate a boolean expression
node = parser.parseExpression("5 > 3 and 2 == 2");
Boolean bool = (Boolean) parser.evaluate(node);  // true
```

## Features

### Arithmetic Operations
```java
parser.evaluate(parser.parseExpression("2 + 3"));      // 5.0
parser.evaluate(parser.parseExpression("10 - 3"));     // 7.0
parser.evaluate(parser.parseExpression("3 * 4"));      // 12.0
parser.evaluate(parser.parseExpression("5 / 2"));      // 2.5
parser.evaluate(parser.parseExpression("-5"));         // -5.0 (unary minus)
```

### Built-in Functions and Constants
```java
parser.evaluate(parser.parseExpression("sin(PI/2)"));  // 1.0
parser.evaluate(parser.parseExpression("cos(0)"));     // 1.0
parser.evaluate(parser.parseExpression("sqrt(16)"));   // 4.0
parser.evaluate(parser.parseExpression("log(E)"));     // 1.0
parser.evaluate(parser.parseExpression("exp(1)"));     // 2.718...
parser.evaluate(parser.parseExpression("tan(PI/4)"));  // 1.0
```

### Boolean Expressions
```java
parser.evaluate(parser.parseExpression("true and false"));   // false
parser.evaluate(parser.parseExpression("true or false"));    // true
parser.evaluate(parser.parseExpression("!false"));           // true
parser.evaluate(parser.parseExpression("5 == 5"));           // true
parser.evaluate(parser.parseExpression("3 < 5"));            // true
parser.evaluate(parser.parseExpression("5 >= 5"));           // true
```

### Ternary Conditional
```java
parser.evaluate(parser.parseExpression("5 > 3 ? 1 : 2"));    // 1.0
parser.evaluate(parser.parseExpression("5 < 3 ? 1 : 2"));    // 2.0
```

### User-Defined Functions
```java
ExpressionParser parser = new ExpressionParser();

// Register custom functions using lambdas
parser.registerFunction("cube", x -> x * x * x);
parser.registerFunction("atan", Math::atan);
parser.registerFunction("toDegrees", Math::toDegrees);

parser.evaluate(parser.parseExpression("cube(3)"));          // 27.0
parser.evaluate(parser.parseExpression("atan(1)"));          // 0.785...
```

### Complex Expressions
```java
// Trigonometric identity: sin^2(x) + cos^2(x) = 1
parser.evaluate(parser.parseExpression("sin(1)*sin(1) + cos(1)*cos(1)"));  // 1.0

// Nested functions
parser.evaluate(parser.parseExpression("log(exp(1))"));      // 1.0

// Mixed boolean and arithmetic
parser.evaluate(parser.parseExpression("(0 == 0) and (2*2 < 5)"));  // true
```

## Methods

The following methods are provided to parse and evaluate math expressions:

| Method | Description |
|--------|-------------|
| `parseExpression(String expr)` | Parses the expression and returns a tree of Node objects |
| `evaluate(Node p)` | Evaluates the tree, returning Double or Boolean |
| `visit(Node p)` | Returns a string representation of the expression tree |
| `registerFunction(String name, Function<Double, Double> logic)` | Registers a user-defined function |

## Node Structure

The parser builds an abstract syntax tree (AST) using the following node types:

```
Node (abstract base)
├── NodeDouble          - Numeric literals (e.g., 3.14)
├── NodeIdentifier      - Constants (PI, E) and boolean literals (true, false)
│   ├── UnaryNodeIdentifier  - Built-in functions (sin, cos, tan, log, exp, sqrt)
│   └── LambdaFunctionNode   - User-registered functions
├── UnaryNode           - Unary operators (-, !)
├── BinaryNode          - Binary operators (+, -, *, /, and, or, ==, !=, <, <=, >, >=)
└── TernaryNode         - Conditional expression (? :)
```

## Building and Testing

```bash
# Compile and run all tests
mvn test

# Compile only
mvn compile

# Run the visual demo
mvn compile exec:java -Dexec.mainClass=demo.ExpressionVisualizer
```

## Demo

The demo application allows you to enter an expression, see the parse tree visualization, and view the evaluation result or syntax errors.

Parsing the expression `(sin(PI/4)+cos(PI/4))*sqrt(exp((log(2)*tan(PI/4))))` generates this tree:

![Screenshot](images/expression_visualizer.png)

Parsing the expression `(0 == 0) and (0 != 1) and !false or ((true == false) and (2*2 < 5))` generates this tree:

![Screenshot](images/expression_visualizer2.png)

## Syntax Reference

### Operator Precedence (lowest to highest)

1. Ternary conditional (`? :`)
2. Logical OR (`or`)
3. Logical AND (`and`)
4. Equality (`==`, `!=`)
5. Relational (`<`, `<=`, `>`, `>=`)
6. Additive (`+`, `-`)
7. Multiplicative (`*`, `/`)
8. Unary (`-`, `!`)
9. Primary (literals, constants, functions, parentheses)

### Grammar

```
<expression>        ::= <conditional_or> [ "?" <expression> ":" <expression> ]
<conditional_or>    ::= <boolean_term> { "or" <boolean_term> }*
<boolean_term>      ::= <equality> { "and" <equality> }*
<equality>          ::= <relation> [ ("==" | "!=") <relation> ]
<relation>          ::= <simple_expr> [ ("<" | "<=" | ">=" | ">") <simple_expr> ]
<simple_expr>       ::= <term> { ("+" | "-") <term> }*
<term>              ::= <factor> { ("*" | "/") <factor> }*
<factor>            ::= <boolean> | <constant> | <function> "(" <expression> ")"
                      | "(" <expression> ")" | "!" <factor> | "-" <factor> | <number>
<boolean>           ::= "false" | "true"
<constant>          ::= "PI" | "E"
<function>          ::= "sin" | "cos" | "tan" | "log" | "exp" | "sqrt" | <user_function>
<number>            ::= <digit>+ [ "." <digit>* ]
```
