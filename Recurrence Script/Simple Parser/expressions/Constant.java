package expressions;
import general.Operation;

public class Constant extends Expression{
	long actualValue;
	public Constant(Operation op, long input) {
		super(op);
		actualValue = input;
		op = Operation.CONSTANT;
	}
	public long evaluate(long... input) {
		assert op == Operation.CONSTANT;
		return actualValue;
	}
}
