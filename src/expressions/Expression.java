package expressions;
import general.Operation;

public class Expression{
	// Binary Tree Expression
	// Expressions are like anonymous functions in a sense
	public Operation op = Operation.IDENTITY;
	
	// Ugly, but usable? Parameters of operation depending on type
	// Should use inheritance here LMFAO
	// Constant, FunctionCall, Paramter
	// Expression recurses on itself so thats good
	public Expression left, right;	
	
	public Expression(Operation op) {
		this.op = op;
	}
	
	
	public Expression(Expression left, Operation op, Expression right) {
		this.left=left;
		this.op=op;
		this.right=right;
	}
	
	public long evaluate(long... input) {
		if (op == Operation.ADD) {
			return left.evaluate(input) + right.evaluate(input);
		}
		else if (op == Operation.SUB) {
			return left.evaluate(input) - right.evaluate(input);
		}
		else if (op == Operation.MUL) {
			return left.evaluate(input) * right.evaluate(input);
		}
		else if (op == Operation.DIV) {
			return left.evaluate(input) / right.evaluate(input);
		}
		error("This is a standard expression. Perhaps you meant to call a subclass?");
		return -1;
	}
	
	private void error(String errmsg) {
		System.err.println(errmsg);
		System.exit(-1);
	}
}
