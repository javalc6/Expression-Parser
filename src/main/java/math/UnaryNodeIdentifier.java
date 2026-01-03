package math;

import java.util.Set;

/**
 * Node element holding unary (one child) identifier
 */

public class UnaryNodeIdentifier extends NodeIdentifier {
    public final static Set<String> functionSet = Set.of("sin", "cos", "tan", "sqrt", "log", "exp");

	protected Node child;

	UnaryNodeIdentifier(String identifier) {
		super(identifier);
		if (!functionSet.contains(identifier))
			throw new IllegalArgumentException("unknown identifier: " + identifier);
	}

    @Override
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

    @Override
	public void visit(StringBuilder sb) {
		sb.append(identifier);
		sb.append('(');
		child.visit(sb);
		sb.append(')');
	}

	public void set(String identifier) {
		if (!functionSet.contains(identifier))
			throw new IllegalArgumentException("unknown identifier: " + identifier);
		this.identifier = identifier;
	}

	public Node getChild() {
		return child;
	}
}//end of class NodeIdentifier
