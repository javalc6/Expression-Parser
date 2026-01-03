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

    @Override
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

    @Override
	public void visit(StringBuilder sb) {
		switch(type) {
			case minus:
				sb.append('-');
				child.visit(sb);
				break;
			case not:
				sb.append('!');
				child.visit(sb);
				break;
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}

	public Node getChild() {
		return child;
	}

	public String getNodeAsString() {
		switch(type) {
			case minus:
				return "-";
			case not:
				return "!";
			default://will never happen
				return type.toString();
		}
	}

}//end of class Node
