package edu.develop.homework;

/**
 * @title ��Ȼ��n�Ľ׳�n!
 * @author ��ܰ��
 */

public class factorial{
	
	/**
	 * @param n
	 * @return n�׳�
	 */
	private static int factorial(int n){
		if(n < 0)
			return -1;
		if(n == 0 || n==1)
			return 1;
		else{
			return n * factorial(n-1);
		}
	}
	
	 public static void main(String[] args){
		 
		 if(args.length < 1){
			 System.out.println("�봫������n");
		 }
		 else{
			 try { 
		         int n = Integer.parseInt(args[0]);  
		         if(n < 0)
		        	 System.out.println("������������");
		         else
		        	 System.out.println(factorial(n));
		     } catch (NumberFormatException e) {  
		         System.out.println("����������");
		     } 
			 
		 }
	}
}