package interpreter;
import java.util.ArrayList;
import expressions.Constant;
import expressions.Expression;
import expressions.FunctionCall;
import expressions.Parameter;
import general.Function;
import general.Operation;
import general.Value;

public class Parser {
	private Util u;	
	private void error(String error) {
		System.err.println(error);
		System.exit(-1);
	}
	
	public Parser(String inputFile) {
		u = new Util(inputFile);
		program_list();
		
	}

	public Parser(String plainText, int mode){
		// if there is a mode, then make that mode the mode thing
		u = new Util(plainText, mode);
		program_list();
	}

	// Beginning of Expression Parsing
	
	private char letter() {
		return u.match_char();
	}

	private char digit() {
		return u.match_digit();
	}
	
	private String var_begin() {
		if (!u.is_char()) System.err.println("Expected Character");
		return letter() + var();		
	}

	private String var() {
		if (u.is_char()) return letter() + var();
		if (u.is_digit()) return digit() + var();
		return ""; // epsilon transition
	}
	
	private String number_begin() {
		if (!u.is_digit()) System.err.println("Expected Digit");
		return digit() + number();
	}
	
	private String number() {
		if (u.is_digit()) return digit() + number();
		return ""; // epsilon transition
	}
	
	private Expression value() {
		if (u.is_digit()) return new Constant(Operation.CONSTANT, Long.parseLong(number_begin()));
		if (u.is_char()) {
			String curVar = var_begin();
			if (!Interpreter.hasVariable(curVar)) {
				error("Did not have variable");
			}
			if (Interpreter.checkType(curVar, Value.FUNCTION)) {
			//	u.match("(") && expr() && u.match(")");
				u.match("(");
				ArrayList<Expression> inputs = expr_list();
				u.match(")");
				return new FunctionCall(Operation.FUNCTION, curVar, inputs);
			}	
			else if (Interpreter.checkType(curVar, Value.PARAM)) {
				return new Parameter(Operation.PARAM, curVar);
			}
		}
		error("Did not find Value");
		return null;
	}	
		
	private Expression term() {
		Expression root = value();
		for (; u.find("/") || u.find("*");) {
			Operation op = Operation.IDENTITY;
			if (u.find("*")) {
				u.match("*");
				op = Operation.MUL;
			}
			else if (u.find("/")) {
				u.match("/");
				op = Operation.DIV;
			}
			Expression reroot = new Expression(root, op, value());
			root = reroot;
		}
		return root;
	}
	
	private Expression expr() {
		Expression root = term();
		for (;u.find("-") || u.find("+");) {
			Operation op = Operation.IDENTITY;
			if (u.find("-")) {
				u.match("-");
				op = Operation.SUB;
			}
			else if (u.find("+")) {
				u.match("+");
				op = Operation.ADD;
			}
			Expression reroot = new Expression(root, op, term());
			root = reroot;
		}
		return root;
	}
	
	// End of Expression Parsing
	
	private ArrayList<Expression> expr_list(){
		ArrayList<Expression> res = new ArrayList<>();
		if (u.is_char() || u.is_digit()) {
			Expression cur;
			for (;;) {
				cur = expr();
				res.add(cur);
				if (!u.find(",")) break;
				u.match(",");
			}
		}
		return res;
	}
	
	private ArrayList<Expression> map() {
		// Expecting a Expression(Long), expr() tuple
		u.match("map");
		u.match("(");
		ArrayList<Expression> list = expr_list();
		u.match(")");
		u.match("->");
		list.add(expr());
		u.match(";");
		return list;
	}
	
	private ArrayList<ArrayList<Expression>> map_list() {
		ArrayList<ArrayList<Expression>> result = new ArrayList<ArrayList<Expression>>();
		if (u.find("map")) {
			ArrayList<Expression> head = map();
			result = map_list();
			result.add(head);
		}
		return result;
	}
	
	private ArrayList<String> var_list(){
		ArrayList<String> res = new ArrayList<>();
		if (u.is_char()) {
			String param;
			for (;;) {
				param = var_begin();
				if (Interpreter.hasVariable(param)) error("Paramater name used already");
				Interpreter.addName(param, Value.PARAM);
				res.add(param);
				if (!u.find(",")) break;
				u.match(",");
			}
		}
		return res;
	}
	
	private Function function() {
		u.match("fun");
		
		String fun_name = var_begin();
		if (Interpreter.hasVariable(fun_name)) error("Function name used already");
		Interpreter.addName(fun_name, Value.FUNCTION);
		Function fxn = new Function();
		fxn.setName(fun_name);
		
		u.match("(");
		ArrayList<String> params = var_list();
		u.match(")");
		u.match("=");
		
		fun_body(fxn);
		for (String param: params) Interpreter.removeName(param);
		Interpreter.pushFunction(fxn);
		u.match(";;");
		
		return fxn;
	}
	
	private void fun_body(Function fxn){
		ArrayList<ArrayList<Expression>> mappings = map_list();
		for (ArrayList<Expression> map: mappings) {
			// Last one in map list is the final one
			Expression[] input = new Expression[map.size()];
			for (int i = 0; i < map.size(); i++) {
				input[i] = map.get(i);
			}
			fxn.map(input);
		}
		fxn.setReturnValue(return_statement());
	}
	
	
	private void print() {
		u.match("print");
		System.out.println(expr().evaluate(null));
		u.match(";;");
		
	}
	
	private Expression return_statement() {
		u.match("return");
		return expr();
	}
	
	private void program_list() {
		if (u.find("fun")) {
			function();
			program_list();
		}
		else if (u.find("print")) {
			print();
			program_list();
		}
		// epsilon, with a final condition 
		if (!u.atEnd()) error("Supposedly reached end of program, but did not.");
	}
	
}
