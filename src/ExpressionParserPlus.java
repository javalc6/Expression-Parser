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

27-12-2022: initial release: storing expression as generic tree

*/
import java.util.HashMap;

public class ExpressionParserPlus {

	final static int MaxNumOfDigit = 38; // max number of digits for numbers

	public static void main(String[] args) {//demo part of the ExpressionParserPlus class
		ExpressionParserPlus ep = new ExpressionParserPlus();
		try {
			Node p1 = ep.parseExpression("log(exp((sin(PI/4)+cos(PI/4))/(sqrt(2)*tan(PI/4))))");
			ep.visit(p1);	
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p1));//expected result: 1.0
			System.out.println();

			Node p2 = ep.parseExpression("(0 == 0) and (0 != 1) and !false or ((true == false) and (2*2 < 5))");
			ep.visit(p2);
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p2));//expected result: true
			System.out.println();

			Node p3 = ep.parseExpression("5 > 4 ? 1 + 2 + 3 : 2 * 2");
			ep.visit(p3);
			System.out.println();
			System.out.println("Result: "+ep.evaluate(p3));//expected result: 6
			System.out.println();

		
		} catch (Exception ex) {
			System.err.println("Exception: " + ex.getMessage());//ex.printStackTrace();
		}
	}

//public method that evaluates an expression stored in tree 'p', returns either Double or Boolean
	public Object evaluate(Node p) {//@NonNull Node p
		return p.evaluate();
	}

//public method that visits expression stored in tree 'p'
	public void visit(Node p) {//@NonNull Node p
		p.visit();
	}

//public method that returns the parsed expression as a tree
	public Node parseExpression(String expr) throws Exception {
		StringHolder sh = new StringHolder(expr);
		Node result = expression(sh);
		if (sh.chars_available())
			throw new Exception("unexpected characters: " + expr.substring(sh.pointer));
		return result;
	}

//protected area
	protected Node expression(StringHolder sh) throws Exception {
		return conditional_expression(sh);
	}

	protected Node conditional_expression(StringHolder sh) throws Exception {
		Node pcoe = conditional_or_expr(sh);
		if (sh.nextSequence("?")) {
			Node pe = expression(sh);
			if (sh.nextSequence(":")) {
				TernaryNode p = new TernaryNode(Type.conditional_expression);
				p.left = pcoe;
				pcoe = p;
				p.center = pe;
				p.right = conditional_expression(sh);
			} else throw new Exception("missing :");
		}
		return pcoe;
	}
	
	protected Node conditional_or_expr(StringHolder sh) throws Exception {
		Node pbt = boolean_term(sh);
		while (sh.nextSequence("or")) {
			BinaryNode p = new BinaryNode(Type.or);
			p.left = pbt;
			pbt = p;
			p.right = boolean_term(sh);
		}
		return pbt;
	}

	protected Node boolean_term(StringHolder sh) throws Exception {
		Node per = equality_relation(sh);
		while (sh.nextSequence("and")) {
			BinaryNode p = new BinaryNode(Type.and);
			p.left = per;
			per = p;
			p.right = equality_relation(sh);
		}
		return per;
	}

	protected Node equality_relation(StringHolder sh) throws Exception {
		Node pre = relation_expression(sh);
		Type type;
		if (sh.nextSequence("==")) {
			type = Type.equal;
		} else if (sh.nextSequence("!=")) {
			type = Type.unequal;
		} else return pre;
		BinaryNode p = new BinaryNode(type);
		p.left = pre;
		pre = p;
		p.right = relation_expression(sh);
		return pre;
	}
	
	protected Node relation_expression(StringHolder sh) throws Exception {
		Node se = simple_expression(sh);
		Type type;
		if (sh.nextSequence(">")) {
			type = Type.gt;
		} else if (sh.nextSequence(">=")) {
			type = Type.gte;
		} else if (sh.nextSequence("<")) {
			type = Type.lt;
		} else if (sh.nextSequence("<=")) {
			type = Type.lte;
		} else return se;
		BinaryNode p = new BinaryNode(type);
		p.left = se;
		se = p;
		p.right = simple_expression(sh);
		return se;
	}
	
	protected Node simple_expression(StringHolder sh) throws Exception {
		Node pt = term(sh);
		Character ch;
		while ((ch = sh.getChar("+-")) != null) {
			BinaryNode p = new BinaryNode(ch == '+' ? Type.add : Type.subtract);
			p.left = pt;
			pt = p;
			p.right = term(sh);
		}
		return pt;
	}

	protected Node term(StringHolder sh) throws Exception {
		Node pf = factor(sh);
		Character ch;
		while ((ch = sh.getChar("*/")) != null) {
			BinaryNode p = new BinaryNode(ch == '*' ? Type.multiply : Type.divide);
			p.left = pf;
			pf = p;
			p.right = factor(sh);
		}
		return pf;
	}

	protected Node factor(StringHolder sh) throws Exception {
		String identifier = sh.getIdentifier();
		if (identifier != null)	{
			if (identifier.equals("false") || identifier.equals("true"))
				return new NodeIdentifier(identifier);
			else if (identifier.equals("PI") || identifier.equals("E"))
				return new NodeIdentifier(identifier);
			else if (identifier.equals("sin") || identifier.equals("cos") || identifier.equals("tan") || identifier.equals("log") || identifier.equals("exp") || identifier.equals("sqrt")) {
				Character ch = sh.getChar("(");
				if (ch != null)	{ // ch == "("
					UnaryNodeIdentifier ni = new UnaryNodeIdentifier(identifier);
					ni.child = expression(sh);
					if (sh.getChar(")") == null)
						throw new Exception("missing ) bracket");
					return ni;
				} else throw new Exception("missing ( bracket");
			} else throw new Exception("unknown identifier: "+identifier);
		} else {
			Character ch = sh.getChar("(!-0123456789");
			if (ch == null)
				if (sh.chars_available())
					throw new Exception("unexpected character: " + sh.expr.charAt(sh.pointer));
				else throw new Exception("unexpected end of expression");
			if (ch == '(') {
				Node n1 = expression(sh);
				if (sh.getChar(")") == null)
					throw new Exception("missing ) bracket");
				return n1;
			} else if (ch == '!') {
				UnaryNode n2 = new UnaryNode(Type.not);
				n2.child = factor(sh);
				return n2;
			} else if (ch == '-') {
				UnaryNode n3 = new UnaryNode(Type.minus);
				n3.child = factor(sh);
				return n3;
			} else if (ch >= '0' && ch <= '9') {
				double num = 0; int nDigits = 0;
				while (ch != null) {
					if (nDigits < MaxNumOfDigit) {
						num = num * 10 + ch - '0';
						nDigits++;
					} else num = num * 10;
					ch = sh.getChar("0123456789");
				}
				if (sh.getChar(".") != null) {//ch != null --> ch == '.'
					double m = 1;
					while ((ch = sh.getChar("0123456789")) != null) {
						m = m * 0.1;
						num = num + (ch - '0') * m;
					}
				}
				return new NodeDouble(num);
			} else return null;
		}
	}

}