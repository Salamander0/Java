import java.io.*;
import java.util.*;

public class maze {
	static int data[][];
	
	public maze(int rows, int columns){
		 data = new int[rows][columns];
	}
	
	//public ArrayList<point>findPath(point start, point end){
		
	//}
	
	public void printout(int rows, int columns){
		for (int i = 0; i < rows; i++) {
		    for (int j = 0; j < columns; j++) {
		        System.out.print(data[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	}
	
	public static void main(String[] args) throws IOException, MazeException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String line = input.readLine();
		StringTokenizer st = new StringTokenizer(line);
		int rows = Integer.parseInt(st.nextToken());
		int columns = Integer.parseInt(st.nextToken());
		
		new maze(rows, columns);
		
		for(int i=0; i<rows; i++){
			Scanner in = new Scanner(System.in);
			for(int j=0; j<columns; j++){
				char ch = in.findInLine(".").charAt(0);
			    	switch(ch){
			    	case '.': data[i][j] = 0;
			    		break;
			    	case 'x': data[i][j] = -1;
			    		break;
			    	case 'a': data[i][j] = -2;
			    		break;
			    	case 'b': data[i][j] = -3;
			    		break;
			    	default: throw new MazeException("invalid input");
			    	}
			}
		}
		
	}
	
}