public class NodeIdentifier extends Node {//Node element holding an identifier without child
	protected String identifier;

	NodeIdentifier(String identifier) {//node constructor of type identifier
		super(Type.identifier);
		this.identifier = identifier;
	}

	public Object evaluate() {
		if (identifier.equals("PI"))
			return Math.PI;
		else if (identifier.equals("E"))
			return Math.E;
		else if (identifier.equals("false"))
			return false;
		else if (identifier.equals("true"))
			return true;
		else throw new RuntimeException("unknown identifier: " + identifier);
	}

	public void visit() {
		System.out.print(identifier);
	}

}//end of class NodeIdentifier
