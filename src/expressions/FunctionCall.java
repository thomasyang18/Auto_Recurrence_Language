package expressions;
import java.util.ArrayList;

import general.Function;
import general.Operation;
import interpreter.Interpreter;

public class FunctionCall extends Expression{
	String fxnCall;
	ArrayList<Expression> params;

	public FunctionCall(Operation op, String input, ArrayList<Expression> params) {
		super(op);
		fxnCall = input;
		this.params = params;
	}
	
	public long evaluate(long... input) {
		assert op == Operation.FUNCTION;
		Function fxn = Interpreter.getFunction(fxnCall);
		long[] pass = new long[params.size()];
		for (int i = 0; i < pass.length; i++) 
			pass[i] = params.get(i).evaluate(input);
		return fxn.evaluate(pass);
	}
}
