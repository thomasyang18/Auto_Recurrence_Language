package interpreter;
import java.util.HashMap;

import general.Function;
import general.Value;

public final class Interpreter {
	// This builds functions
	private static HashMap<String, Value> var_type= new HashMap<String, Value>();
	private static HashMap<String, Function> functions = new HashMap<String, Function>();
	private static HashMap<String, Integer> param_positions = new HashMap<>();
	
	
	public Interpreter() {
		System.err.println("ERROR: Do not build Interpreter");
		System.exit(-1);
	}
	
	public static boolean hasVariable(String input) {
		return var_type.containsKey(input);
	}
	
	public static boolean checkType(String input, Value v) {
		assert var_type.containsKey(input);
		return var_type.get(input) == v;
	}
	
	public static void pushFunction(Function func) {
		functions.put(func.getName(), func);
	}
	
	public static Function getFunction(String func) {
		return functions.get(func);
	}
	
	public static boolean addName(String input, Value v) {
		// Value type is immutable, so don't allow another add
		if (hasVariable(input)) return false;
		var_type.put(input, v);
		if (v == Value.PARAM) {
			param_positions.put(input, param_positions.size());
		}
		return true;
	}
	
	public static int getPositionOfVariable(String input) {
		return param_positions.get(input);
	}
	
	public static boolean removeName(String input) {
		if (!hasVariable(input)) return false;
		if (var_type.get(input) == Value.FUNCTION) functions.remove(input);
		else if (var_type.get(input) == Value.PARAM) param_positions.remove(input);
		var_type.remove(input);
		return true;
	}
}
