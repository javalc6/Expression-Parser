package math;

/**
 * Node element holding an identifier without child
 */

public class NodeIdentifier extends Node {
	protected String identifier;

	NodeIdentifier(String identifier) {
		super(Type.identifier);
		this.identifier = identifier;
	}

    @Override
	public Object evaluate() {
        switch (identifier) {
            case "PI":
                return Math.PI;
            case "E":
                return Math.E;
            case "false":
                return false;
            case "true":
                return true;
            default:
                throw new RuntimeException("unknown identifier: " + identifier);
        }
	}

    @Override
	public void visit(StringBuilder sb) {
		sb.append(identifier);
	}

    @Override
	public String getNodeAsString() {
		return identifier;
	}
}//end of class NodeIdentifier
