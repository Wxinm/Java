package edu.develop.homework;

import java.util.Arrays;

/**
 * @title ����
 * @author ��ܰ��
 * @description ��������������Ϊmain��������
 */
public class sort{
	 public static void main(String[] args){
		 
		 int[] sorted = new int[args.length];
		 
		 //String����תint����
		 int i = 0;
		 for(String para:args){
			 try { 
		         sorted[i++] = Integer.parseInt(para);  
		     } catch (NumberFormatException e) {  
		         System.out.println("���������,�����˳�");
		         System.exit(1);
		     } 
		 }
		 
		 //ð������
		 for(i = 0; i < sorted.length-1; i++){
			 for(int j = 0; j < sorted.length-1-i; j++){
				 if(sorted[j] > sorted[j+1]){
					 int temp = sorted[j];
					 sorted[j] = sorted[j+1];
					 sorted[j+1] = temp;
				 }
			 }
		 }
	 	 
		 //��ӡ�ź��������
		 System.out.println("Bubble Sort:"+Arrays.toString(sorted));
		 
	}
}