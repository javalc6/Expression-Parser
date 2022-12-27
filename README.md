# Expression Parser
The class ExpressionParserPlus implements expression parser. It supports parsing of expressions with numbers and booleans values.

Numeric expressions may contain brackets ( ), operators *,-,/,+,"and" and "or" and numbers with optional decimal point and values "true" and "false".
In addition it is possible to use constants "PI" and "E", functions sin(), cos(), tan(), log(), exp(), sqrt().

# Methods
The following methods are provided to parse and evaluate math expressions:
```
parseExpression(String expr): parses the given expression and returns a binary tree representing the parse expression;

evaluate(Node p): evaluates the binary tree containing an expression;

visit(Node p): visits the binary tree containing an expression;
```

# Syntax Rules
The parser use the following rules to parse math expressions:
```
<expression> ::= <conditional or expr> [ "?" <expression> ":" <expression> ]
<conditional or expr> ::= <boolean term> { "or" <boolean term> }*
<boolean term> ::= <equality relation> { "and" <equality relation> }*
<equality relation> ::= <relation expression> "==" <relation expression> | <relation expression> "!=" <relation expression> | <relation expression>
<relation expression> ::= <simple expression> "<" <simple expression> | <simple expression> "<=" <simple expression> | <simple expression> ">=" <simple expression> | <simple expression> ">" <simple expression>
<simple expression> ::= <term> { ("+"|"-") <term> }*
<term> ::= <factor> { ("*"|"/") <factor> }*
<factor> ::= <boolean> | <constant> | <unary function> "(" <expression> ")" | "(" <expression> ")" | "!" <factor> | "-" <factor> | <number>
<boolean> ::= "false" | "true"
<constant> == "PI" | "E"
<unary function> == "sin" | "cos" | "tan" | "log" | "exp" | "sqrt"
<number> ::= { <digit> }+ [ "." { <digit> }* ]
<digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

# Example

Parsing the expression (sin(PI\*0.25)+cos(PI\*0.25))*sqrt(2) you obtain the following binary tree:

```
         *
        / \
       /   \
      +    sqrt
     / \     |
  sin   cos  2
  |       |
  *       *
 / \     / \
PI 0.25 PI 0.25
```
The evaluation of this binary tree returns the value 2, as expected.

