//import java.io.*;
import java.util.*;

public class HelloWorld
{ 
  public static void main(String args[]){ 
	  Random   rd=new Random();
	  int ia[] = new int[100];
	  int sa[] = new int[100];
	  
	  for(int i=0; i<ia.length; i++){
		  ia[i]=rd.nextInt(100);
	  }
	  
	  System.arraycopy( ia, 0, sa, 0, ia.length );
	  
	  System.out.println(Arrays.toString(ia));
	  bubblesort(ia);
	  exchangesort(sa);
	  System.out.println(Arrays.toString(ia));
	  System.out.println(Arrays.toString(sa));
  }
  
  public static void bubblesort(int array[]){
	 int temp;
	 boolean flag = true;
	 
	 while(flag){
		 flag = false;
		 for(int i=0; i<array.length-1; i++){
			 if(array[i]>array[i+1]){
				 temp=array[i];
				 array[i]=array[i+1]; 
				 array[i+1]=temp;
				 flag=true;
			 }
		 }
	 }
  }
  
  public static void exchangesort(int array[]){
	  int temp;
	  
	  for(int i=0; i<array.length-1; i++){
		  for(int j=i+1; j<array.length; j++){
			  if(array[i]>array[j]){
				  temp=array[i];
				  array[i]=array[j];
				  array[j]=temp;
			  }
		  }
	  }
  }
}