package edu.develop.homework;

/**
 * @title ȫ����
 * @author ��ܰ��
 * @description ��дһ����������ַ���"abcdefgh"��ȫ���С�
 */

public class permutation{
	
	public static void main(String[] args){
		char[] chars={'a','b','c','d','e'};
		int lineNum = factorial(chars.length-1);
		permutate(chars, 0, lineNum);
	}
	
	/**
	 * ȫ����
	 * @param chars char������
	 * @param start ��ʼ����
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
	 * ��������������Ԫ�ؽ���
	 * @param chars char������
	 * @param a Ԫ������
	 * @param b Ԫ������
	 */
	private static void swap(char[] chars, int a, int b){
		char temp = chars[a];
		chars[a] = chars[b];
		chars[b] = temp;
	}
	
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
			
}