package math;

import java.util.Objects;

/**
 * Node element with three children
 */

public class TernaryNode extends Node {
	protected Node left;
	protected Node center;
	protected Node right;

	TernaryNode(Type type) {
		super(type);
		left = null;
		center = null;
		right = null;
	}

	public Object evaluate() {
        //will never happen
        if (Objects.requireNonNull(type) == Type.conditional_expression) {
            if ((Boolean) left.evaluate())
                return center.evaluate();
            else return right.evaluate();
        }
        throw new RuntimeException("unexpected type: " + type);
    }

	public void visit(StringBuilder sb) {
        if (Objects.requireNonNull(type) == Type.conditional_expression) {
			sb.append('(');
			left.visit(sb);
			sb.append(" ? ");
			center.visit(sb);
			sb.append(" : ");
			right.visit(sb);
			sb.append(')');
        } else {
            throw new RuntimeException("unexpected type: " + type);
        }
	}

	public Node getLeft() {
		return left;
	}

	public Node getCenter() {
		return center;
	}

	public Node getRight() {
		return right;
	}

	public String getNodeAsString() {
		return type.toString();
	}

}//end of class Node
