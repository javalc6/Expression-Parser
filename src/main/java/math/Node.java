package math;

/**
 * Abstract node element of the tree
 */

abstract public class Node {
	protected final Type type;

	Node(Type type) {
		this.type = type;
	}

	abstract public Object evaluate();//Note: shall return either Double or Boolean

	abstract public void visit(StringBuilder sb);

	abstract public String getNodeAsString();//returns a string related only to node itself, not to children
}//end of class Node
