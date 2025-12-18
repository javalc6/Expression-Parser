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

	public void visit() {
        //will never happen
        if (Objects.requireNonNull(type) == Type.conditional_expression) {
            System.out.print('(');
            left.visit();
            System.out.print(" ? ");
            center.visit();
            System.out.print(" : ");
            right.visit();
            System.out.print(')');
        } else {
            throw new RuntimeException("unexpected type: " + type);
        }
	}


}//end of class Node
