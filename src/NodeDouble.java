class NodeDouble extends Node {//Node element holding a number
	protected double num;

	NodeDouble(double num) {//node constructor of type number
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
