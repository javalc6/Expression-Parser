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

	public void visit() {
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
				System.out.print('(');
				left.visit();
				System.out.print(" " + type + " ");
				right.visit();
				System.out.print(')');
				break;
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}


}//end of class Node
