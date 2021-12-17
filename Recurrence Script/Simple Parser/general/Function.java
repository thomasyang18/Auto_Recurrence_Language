package general;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import expressions.Expression;

import java.util.ArrayList;
public class Function {
	// For now, only 1 input
	// Functions are evaluated by Interpeter calls
	Expression output;
	TreeMap<long[] , Long> dp = new TreeMap<>(
	// Comparator is only there to map DP to correct spots.
	new Comparator<long[]>(){
		public int compare(long[] a, long[] b) {
			for (int i = 0; i < Math.min(a.length,b.length); i++) {
				if (a[i] == b[i]) continue;
				return Long.compare(a[i], b[i]);
			}
			return a.length-b.length;
		}
	});
	
	ArrayList<Expression[]> map_preimage= new ArrayList<>();
	ArrayList<Expression> map_image = new ArrayList<>();
	
	private String name;
	private Expression returnValue;
	
	// Functions are of form 
	// param
	// base (maps)
	// return statement
	
	public Function() {}
	
	private boolean matches_map(Expression[] preimage, long... input) {
		for (int i = 0; i < input.length; i++) {
			long res = preimage[i].evaluate(input);
			if (res == input[i]) continue;
			return false;
		}
		return true;
	}
	
	public long evaluate(long... input) {
		for (int i = 0; i < map_preimage.size(); i++) {
			// We bet that there aren't 10 billion base unique cases, 
			// so we don't need to use maps and make comparison complicated.
			if (matches_map(map_preimage.get(i), input)) 
				return map_image.get(i).evaluate(input);
		}
		
		if (dp.containsKey(input)) return dp.get(input);
		dp.put(input, returnValue.evaluate(input));
		return dp.get(input);
	}
	
	public void map(Expression... all) {
		// The way I code it, we pass through (long[], long)
		// but it all gets put into one big thing param. So get the 
		// last long as the final mapping.
		Expression[] expr= Arrays.copyOf(all, all.length-1);
		
		map_preimage.add(expr);
		map_image.add(all[all.length-1]);
	}
	
	public void setReturnValue(Expression o) {
		returnValue = o;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String o) {
		name = o;
	}
}
