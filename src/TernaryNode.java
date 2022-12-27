public class TernaryNode extends Node {//Node element with three childs
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
		switch(type) {
			case conditional_expression:
				if ((Boolean)left.evaluate())
					return center.evaluate();
				else return right.evaluate();
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}

	public void visit() {
		switch(type) {
			case conditional_expression:
				System.out.print('(');
				left.visit();
				System.out.print(" ? ");
				center.visit();
				System.out.print(" : ");
				right.visit();
				System.out.print(')');
				break;
			default://will never happen
				throw new RuntimeException("unexpected type: " + type);
		}
	}


}//end of class Node
