package edu.develop.homework;

/**
 * @title 全排列
 * @author 王馨漫
 * @description 编写一个程序，输出字符串"abcdefgh"的全排列。
 */

public class permutation{
	
	public static void main(String[] args){
		char[] chars={'a','b','c','d','e'};
		int lineNum = factorial(chars.length-1);
		permutate(chars, 0, lineNum);
	}
	
	/**
	 * 全排序
	 * @param chars char型数组
	 * @param start 开始索引
	 */
	private static int count = 0;
	public static void permutate(char[] chars, int start, int lineNum){
		if(start == chars.length){
			System.out.print(String.valueOf(chars));
			System.out.print(" ");
			count++;
			if(count%lineNum == 0)
				System.out.println();
		}
		else{
			for(int i = start; i < chars.length; i++){
				swap(chars, start, i);
				permutate(chars, start+1, lineNum);
				swap(chars, start, i);
			}
		}
	}
	
	/**
	 * 数组中任意两个元素交换
	 * @param chars char型数组
	 * @param a 元素索引
	 * @param b 元素索引
	 */
	private static void swap(char[] chars, int a, int b){
		char temp = chars[a];
		chars[a] = chars[b];
		chars[b] = temp;
	}
	
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
			
}