package interpreter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Util {
	
	final boolean debug_flag = false;
	
	private void error(String val) {
		System.err.println(val);
		System.exit(-1);
	}
	
	public char[] inputString;
	public int toki = 0; // token index

	public Util(String inStr, int mode){
		StringBuilder inputBuilder = new StringBuilder();
		// mode just means that the input is a string
		char[] delimiters = {' ', '\n', 10, 13, 9}; // Numbers are guesses at enter
		outer: for (char ch: inStr.toCharArray()) {
			for (char delim: delimiters) if (ch == delim) continue outer;
			inputBuilder.append(ch);
		}
		inputString = inputBuilder.toString().toCharArray();
	}
	public Util(String inputFile) {
		StringBuilder inputBuilder = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String s;
			char[] delimiters = {' ', '\n', 10, 13, 9}; // Numbers are guesses at enter
			while ((s=br.readLine()) != null) {
				outer: for (char ch: s.toCharArray()) {
					for (char delim: delimiters) if (ch == delim) continue outer;
					inputBuilder.append(ch);
				}
			}
			inputString = inputBuilder.toString().toCharArray();
			br.close();
		}
		catch (IOException e) {
			System.err.println(e.getStackTrace());
		}
	}
	// Peeking functions
	
	public boolean find(String needed) {
		// Find is needed when the FIRST of this set contains the first
		// of another set and you need to peek
		// if (debug_flag) System.out.println("Find " + needed);
		
				
		for (int i = 0; i < needed.length(); i++) {
			if (toki+i >= inputString.length) return false;
			if (needed.charAt(i) != inputString[toki+i]) return false;
		}
		return true;
	}
	
	public boolean is_char() {
		if (toki >= inputString.length) return false;
		return (inputString[toki] >= 'a' && inputString[toki] <= 'z') ||
				(inputString[toki] >= 'A' && inputString[toki] <= 'Z');
	}
	public boolean is_digit() {
		if (toki >= inputString.length) return false;
		return inputString[toki] >= '0' && inputString[toki] <= '9';
	}
	
	// Matcher functions
	
	public String match(String needed) {
		if (debug_flag) System.out.println("Match " + needed);
		if (!find(needed)) error("Did not find the variable " + needed);
		toki+=needed.length();
		return needed;
	}
	
	public char match_char() {
		if (debug_flag) System.out.println("Matching char");
		if (!is_char()) error("Did not find character");
		return inputString[toki++];
	}
	public char match_digit() {
		if (debug_flag) System.out.println("Matching digit");
		if (!is_digit()) error("Did not find digit");
		return inputString[toki++];
	}
	
	public boolean atEnd() {

		return toki == inputString.length;
	}
	
}
