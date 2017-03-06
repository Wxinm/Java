package edu.develop.homework;

/**
 * @title 自然数n的阶乘n!
 * @author 王馨漫
 */

public class factorial{
	
	/**
	 * @param n
	 * @return n阶乘
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
			 System.out.println("请传参输入n");
		 }
		 else{
			 try { 
		         int n = Integer.parseInt(args[0]);  
		         if(n < 0)
		        	 System.out.println("请输入正整数");
		         else
		        	 System.out.println(factorial(n));
		     } catch (NumberFormatException e) {  
		         System.out.println("请输入整数");
		     } 
			 
		 }
	}
}