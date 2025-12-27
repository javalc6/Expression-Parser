/* Class that implements expression parser. It supports parsing of expressions with numbers and booleans values.

Numeric expressions may contain brackets ( ), operators *,-,/,+,"and" and "or" and numbers with optional decimal point and values "true" and "false".
In addition it is possible to use constants "PI" and "E", functions sin(), cos(), tan(), log(), exp(), sqrt().

Rules for expressions:
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

*/
package math;

import java.text.ParseException;
import java.util.List;

public class ExpressionParser {
    private List<Token> tokens;
    private int current = 0;

//public method that evaluates an expression stored in tree 'p', returns either Double or Boolean
	public Object evaluate(Node p) {//@NonNull Node p
		return p.evaluate();
	}

//public method that visits expression stored in tree 'p'
    public String visit(Node node) {
        StringBuilder sb = new StringBuilder();
        if (node != null) {
            node.visit(sb);
        }
        return sb.toString();
    }

//public method that returns the parsed expression as a tree
    public Node parseExpression(String expression) throws ParseException {
        Tokenizer tokenizer = new Tokenizer(expression);
        this.tokens = tokenizer.tokenize();
        this.current = 0;
        
        Node result = expression();
        if (!isAtEnd()) {
            Token trailing = peek();
            throw new ParseException("Unexpected characters: " + trailing.value, trailing.position);
        }
        return result;
    }

    private Node expression() throws ParseException {
        Node pcoe = conditional_or_expr();

        if (match(Type.question)) {
            TernaryNode p = new TernaryNode(Type.conditional_expression);
            p.left = pcoe;
            p.center = expression();
            consume(Type.colon, "Missing :");
            p.right = expression();
            return p;
        }
        return pcoe;
    }

    private Node conditional_or_expr() throws ParseException {
        Node pbt = boolean_term();
        while (match(Type.or)) {
            BinaryNode p = new BinaryNode(Type.or);
            p.left = pbt;
            p.right = boolean_term();
            pbt = p;
        }
        return pbt;
    }

    private Node boolean_term() throws ParseException {
        Node per = equality();
        while (match(Type.and)) {
            BinaryNode p = new BinaryNode(Type.and);
            p.left = per;
            p.right = equality();
            per = p;
        }
        return per;
    }

    private Node equality() throws ParseException {
        Node pre = relation_expression();
        while (peek().type == Type.equal || peek().type == Type.unequal) {
            Token op = advance();
            BinaryNode p = new BinaryNode(op.type);
            p.left = pre;
            p.right = relation_expression();
            pre = p;
        }
        return pre;
    }

    private Node relation_expression() throws ParseException {
        Node se = simple_expression();
        if (isRelational(peek().type)) {
            Token op = advance();
            BinaryNode p = new BinaryNode(op.type);
            p.left = se;
            p.right = simple_expression();
            return p;
        }
        return se;
    }

    private Node simple_expression() throws ParseException {
        Node pt = term();
        while (peek().type == Type.add || peek().type == Type.subtract) {
            Token op = advance();
            BinaryNode p = new BinaryNode(op.type);
            p.left = pt;
            p.right = term();
            pt = p;
        }
        return pt;
    }

    private Node term() throws ParseException {
        Node pf = factor();
        while (peek().type == Type.multiply || peek().type == Type.divide) {
            Token op = advance();
            BinaryNode p = new BinaryNode(op.type);
            p.left = pf;
            p.right = factor();
            pf = p;
        }
        return pf;
    }

    private Node factor() throws ParseException {
        if (match(Type.subtract) || match(Type.minus)) {
            UnaryNode node = new UnaryNode(Type.minus);
            node.child = factor();
            return node;
        }
        if (match(Type.not)) {
            UnaryNode node = new UnaryNode(Type.not);
            node.child = factor();
            return node;
        }
        if (match(Type.lparen)) {
            Node node = expression();
            consume(Type.rparen, "Expected ')' after expression");
            return node;
        }
        if (peek().type == Type.number) {
            return new NodeDouble(Double.parseDouble(advance().value));
        }
        
        if (peek().type == Type.boolean_literal) {
            return new NodeIdentifier(advance().value);
        }

        if (peek().type == Type.identifier) {
            Token id = advance();
            if (UnaryNodeIdentifier.functionSet.contains(id.value)) {
                consume(Type.lparen, "Missing ( bracket");
                UnaryNodeIdentifier func = new UnaryNodeIdentifier(id.value);
                func.child = expression();
                consume(Type.rparen, "Missing ) bracket");
                return func;
            }
            return new NodeIdentifier(id.value);
        }

        if (isAtEnd()) {
            throw new ParseException("Unexpected end of expression", peek().position);
        }

        throw new ParseException("Unexpected token: " + peek().value, peek().position);
    }

    private boolean match(Type type) {
        if (peek().type == type) {
            advance();
            return true;
        }
        return false;
    }

    private Token consume(Type type, String message) throws ParseException {
        if (peek().type == type) return advance();
        throw new ParseException(message, peek().position);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == Type.eof;
    }

    private boolean isRelational(Type type) {
        return type == Type.lt || type == Type.lte || type == Type.gt || type == Type.gte;
    }
}