package math;

/**
 * Node element holding a number
 */

public class NodeDouble extends Node {
	protected double num;

	NodeDouble(double num) {
		super(Type.number);
		this.num = num;
	}

	public Object evaluate() {
		return num;
	}

	public void visit(StringBuilder sb) {
		sb.append(num);
	}

	public void set(Double val) {
		num = val;
	}

	public double get() {
		return num;
	}

	public String getNodeAsString() {
		return Double.toString(num);
	}

}//end of class NodeDouble
