package math;

/**
 * Node element holding a number
 */

class NodeDouble extends Node {
	protected final double num;

	NodeDouble(double num) {
		super(Type.number);
		this.num = num;
	}

	public Object evaluate() {
		return num;
	}

	public void visit() {
		System.out.print(num);
	}

}//end of class NodeDouble
