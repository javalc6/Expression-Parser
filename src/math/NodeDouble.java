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

    @Override
	public Object evaluate() {
		return num;
	}

    @Override
	public void visit(StringBuilder sb) {
		sb.append(num);
	}

	public void set(Double val) {
		num = val;
	}

	public double get() {
		return num;
	}

    @Override
	public String getNodeAsString() {
		return Double.toString(num);
	}

}//end of class NodeDouble
