package math;

/**
 * Node element holding unary (one child) identifier
 */

public class UnaryNodeIdentifier extends NodeIdentifier {
	protected Node child;

	UnaryNodeIdentifier(String identifier) {
		super(identifier);
	}

	public Object evaluate() {
        switch (identifier) {
            case "sin":
                return Math.sin((Double) child.evaluate());
            case "cos":
                return Math.cos((Double) child.evaluate());
            case "tan":
                return Math.tan((Double) child.evaluate());
            case "log":
                return Math.log((Double) child.evaluate());
            case "exp":
                return Math.exp((Double) child.evaluate());
            case "sqrt":
                return Math.sqrt((Double) child.evaluate());
            default:
                throw new RuntimeException("unknown identifier: " + identifier);
        }
	}

	public void visit() {
		System.out.print(identifier);
		System.out.print("(");
		child.visit();
		System.out.print(")");
	}

}//end of class NodeIdentifier
