package zkouska;


import java.io.*;
import java.util.*;

/*
class A{
	static public void main(String []args) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("/Users/daprok/Desktop/untitled text 3.txt"));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		System.out.println(lines);
	}
}
*/

class A{
	static public void main(String []args){
		List<Integer> a = new ArrayList<Integer>();
		for(int i=1; i<=10;i++){a.add(a.size(),i);}
		int tenth = a.get(9);
		System.out.println(a.size() + "tenth:" + tenth);
	}
}
