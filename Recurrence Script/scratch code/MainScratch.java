import java.io.*;
import java.util.*;
import static java.lang.Math.*;

public class MainScratch{
	
	public static void main(String[] args) throws IOException{
		// br = new BufferedReader(new FileReader(".in"));
		// out = new PrintWriter(new FileWriter(".out"));
		// new Thread(null, new (), "fisa balls", 1<<28).start();
		
		out.close();
	}
	
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
	static StringTokenizer st = new StringTokenizer("");
	static String read() throws IOException{
		while (!st.hasMoreElements()) st = new StringTokenizer(br.readLine());
		return st.nextToken();
	}
	public static int readInt() throws IOException{return Integer.parseInt(read());}
	public static long readLong() throws IOException{return Long.parseLong(read());}
}