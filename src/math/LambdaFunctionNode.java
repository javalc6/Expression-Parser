package math;

import java.util.function.Function;
/**
 * Node element holding user defined lambda function
 */

public class LambdaFunctionNode extends NodeIdentifier {
	private Node child;
	private final Function<Double, Double> lambda;

    public LambdaFunctionNode(String identifier, Node argument, Function<Double, Double> lambda) {
        super(identifier);
        this.child = argument;
        this.lambda = lambda;
    }

    @Override
    public Object evaluate() {
        Object val = child.evaluate();
        if (!(val instanceof Double)) {
            throw new RuntimeException("Function '" + identifier + "' expects a numeric argument, but got: " + 
                                       (val == null ? "null" : val.getClass().getSimpleName()));
        }
        return lambda.apply((Double) val);
    }

    @Override
	public void visit(StringBuilder sb) {
		sb.append(identifier);
		sb.append('(');
		child.visit(sb);
		sb.append(')');
	}

	public Node getChild() {
		return child;
	}
}//end of class LambdaFunctionNode
