import java.util.*;


public class factorization {
	public static int factor(int number, int divider, List<Integer> dividers){
		while (number % divider == 0){
			number /= divider;
			dividers.add(divider);
		}
		return number;
	}
	
	public static void main(String[] args) {
		int number = 84567;
		int max = (int) Math.sqrt(number);
		
		List<Integer> dividers = new ArrayList<Integer>();
		
		int work_number = number;
		work_number = factor(number, 2, dividers);
		
		for(int i=3; i<=max; i+=2){
			work_number = factor(work_number, i, dividers);
		}
		
		System.out.println("Factorized number: " + number);
		System.out.print("Result: ");
		
		for(int divider : dividers){
			System.out.print(divider + " ");
		}
		if(work_number>1){
			System.out.print(work_number + " ");
		}
	}
}
