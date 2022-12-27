public class UnaryNodeIdentifier extends NodeIdentifier {//Node element holding unary (one child) identifier
	protected Node child;

	UnaryNodeIdentifier(String identifier) {//node constructor of type unary identifier
		super(identifier);
	}

	public Object evaluate() {
		if (identifier.equals("sin"))
			return Math.sin((Double)child.evaluate());
		else if (identifier.equals("cos"))
			return Math.cos((Double)child.evaluate());
		else if (identifier.equals("tan"))
			return Math.tan((Double)child.evaluate());
		else if (identifier.equals("log"))
			return Math.log((Double)child.evaluate());
		else if (identifier.equals("exp"))
			return Math.exp((Double)child.evaluate());
		else if (identifier.equals("sqrt"))
			return Math.sqrt((Double)child.evaluate());
		else throw new RuntimeException("unknown identifier: " + identifier);
	}

	public void visit() {
		System.out.print(identifier);
		System.out.print("(");
		child.visit();
		System.out.print(")");
	}

}//end of class NodeIdentifier
