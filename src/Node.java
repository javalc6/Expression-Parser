abstract public class Node {//Abstract node element of the tree
	protected final Type type;

	Node(Type type) {//generic node constructor
		this.type = type;
	}

	abstract public Object evaluate();//returns either Double or Boolean

	abstract public void visit();

}//end of class Node
