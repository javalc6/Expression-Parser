package math;

/**
 * Node element with two children
 */

public class BinaryNode extends Node {
	protected Node left;
	protected Node right;

	BinaryNode(Type type) {
		super(type);
		left = null;
		right = null;
	}

	public Object evaluate() {
		switch(type) {
			case add:
				return (Double)left.evaluate() + (Double)right.evaluate();
			case subtract:
				return (Double)left.evaluate() - (Double)right.evaluate();
			case multiply:
				return (Double)left.evaluate() * (Double)right.evaluate();
			case divide:
				return (Double)left.evaluate() / (Double)right.evaluate();
			case or:
				return (Boolean)left.evaluate() || (Boolean)right.evaluate();
			case and:
				return (Boolean)left.evaluate() && (Boolean)right.evaluate();
			case equal:
				return left.evaluate().equals(right.evaluate());
			case unequal:
				return !left.evaluate().equals(right.evaluate());
			case lt:
				return (Double)left.evaluate() < (Double)right.evaluate();
			case lte:
				return (Double)left.evaluate() <= (Double)right.evaluate();
			case gt:
				return (Double)left.evaluate() > (Double)right.evaluate();
			case gte:
				return (Double)left.evaluate() >= (Double)right.evaluate();
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}

	public void visit(StringBuilder sb) {
		switch(type) {
			case add:
			case subtract:
			case multiply:
			case divide:
			case and:
			case or:
			case equal:
			case unequal:
			case lt:
			case lte:
			case gt:
			case gte:
				sb.append('(');
				left.visit(sb);
				sb.append(" ").append(getNodeAsString()).append(" ");
				right.visit(sb);
				sb.append(')');
				break;
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public String getNodeAsString() {
		switch(type) {
			case add:
				return "+";
			case subtract:
				return "-";
			case multiply:
				return "*";
			case divide:
				return "/";
			case and:
				return "and";
			case or:
				return "or";
			case equal:
				return "==";
			case unequal:
				return "!=";
			case lt:
				return "<";
			case lte:
				return "<=";
			case gt:
				return ">";
			case gte:
				return ">=";
			default://will never happen
				return type.toString();
		}
	}

}//end of class Node
