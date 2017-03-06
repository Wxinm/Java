package edu.develop.homework;

import java.util.Arrays;

/**
 * @title 排列
 * @author 王馨漫
 * @description 按升序排序，输入为main函数参数
 */
public class sort{
	 public static void main(String[] args){
		 
		 int[] sorted = new int[args.length];
		 
		 //String数组转int数组
		 int i = 0;
		 for(String para:args){
			 try { 
		         sorted[i++] = Integer.parseInt(para);  
		     } catch (NumberFormatException e) {  
		         System.out.println("输入非数字,程序退出");
		         System.exit(1);
		     } 
		 }
		 
		 //冒泡排序
		 for(i = 0; i < sorted.length-1; i++){
			 for(int j = 0; j < sorted.length-1-i; j++){
				 if(sorted[j] > sorted[j+1]){
					 int temp = sorted[j];
					 sorted[j] = sorted[j+1];
					 sorted[j+1] = temp;
				 }
			 }
		 }
	 	 
		 //打印排好序的数组
		 System.out.println("Bubble Sort:"+Arrays.toString(sorted));
		 
	}
}