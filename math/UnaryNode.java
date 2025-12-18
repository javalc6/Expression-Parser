package math;

/**
 * Node element with one child
 */

public class UnaryNode extends Node {
	protected Node child;

	UnaryNode(Type type) {
		super(type);
		child = null;
	}

	public Object evaluate() {
		switch(type) {
			case minus:
				return -(Double)child.evaluate();
			case not:
				return !(Boolean)child.evaluate();
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}

	public void visit() {
		switch(type) {
			case minus:
				System.out.print('-');
				child.visit();
				break;
			case not:
				System.out.print('!');
				child.visit();
				break;
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}


}//end of class Node
