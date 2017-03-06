package edu.develop.homework;

import java.util.Scanner;
/**
 * @title 计算矩形面积和周长
 * @author 王馨漫
 * 
 */

/*
创建一个简单的表示矩形的Rectangle类，满足以下条件：

（1）定义两个成员变量height和width，表示矩形的长和宽，类型为整型 
（2）定义一个getArea方法，返回矩形的面积 
（3）定义一个getPerimeter方法，返回矩形的周长 
（4）在main函数中，利用输入的2个参数分别作为矩形的长和宽，调用getArea和getPermeter方法，计算并返回矩形的面积和周长

 输入：
 输入2个正整数，中间用空格隔开，分别作为矩形的长和宽，例如：5 8

 输出：
 输出2个正整数，中间用空格隔开，分别表示矩形的面积和周长，例如：40 26
*/
public class rectangle{
	
	private int height;
	private int width;
	
	public rectangle(int height, int width){
		this.height = height;
		this.width = width;
	}
	
	public int getArea(){
		return this.height * this.width;
	}
	
	public int getPerimeter(){
		return 2 * (height + width);
	}
	
	
	public static void main(String[] args){
        
		System.out.println("请输入整数长和宽，用空格间隔："); 
        //获得长和宽的输入
        Scanner input=new Scanner(System.in); 
        String para=input.nextLine();
        
        String[] para_arr = para.split(" ");
        if(para_arr.length != 2){
        	System.out.println("请输入两个参数");
        }
        else{
        	try{ 
        		int height = Integer.parseInt(para_arr[0]); 
        		int width = Integer.parseInt(para_arr[1]); 
        		
        		if(height <= 0 || width <= 0){
        			System.out.println("请输入正整数");
        		}
        		else{
        			rectangle rec = new rectangle(height, width);
        			System.out.println(rec.getArea()+" "+rec.getPerimeter());
//        			System.out.println("面积为："+rec.getArea());
//        			System.out.println("周长为："+rec.getPerimeter());
        		}
        	} catch (NumberFormatException e) {  
        		System.out.println("请输入整数");
        	} 
        	
        }
 
	}
}