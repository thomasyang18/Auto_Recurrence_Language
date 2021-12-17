package expressions;
import general.Operation;
import general.Value;
import interpreter.Interpreter;

public class Parameter extends Expression{
	int atPos;
	public Parameter(Operation op, String input) {
		// variable
		super(op);
		if (Interpreter.hasVariable(input)) {
			if (Interpreter.checkType(input, Value.PARAM)) {
				op = Operation.PARAM;
				atPos = Interpreter.getPositionOfVariable(input);
			}
		}
	}
	
	public long evaluate(long... input) {
		assert op == Operation.PARAM;
		return input[atPos];
	}
}
