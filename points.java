package edu.develop.homework;

import java.util.Scanner;

/**
 * @title 计算两点间距离
 * @author 王馨漫
 *
 */
/*
编写一个表示二维平面上的点的类MyPoint，满足以下条件：
（1）定义private的成员变量x和y，表示点的x和y坐标，类型为double
（2）定义两个MyPoint的构造方法，一个构造方法不带参数，而且x和y的初始值为0，另一个构造方法有两个参数，参数名为x和y，类型为double，用这两个参数分别作为初始x和y坐标
（3）定义一个getD方法，有一个类型为MyPoint的对象参数，功能为返回当前对象和参数对象这两个坐标点的距离，返回值为double类型
（4）编写测试的main方法，调用getD计算两个点之间的距离

 输入：
 输入2行数据， 总共4个有理数。每2个数据一组，表示一个点的x和y坐标，每行的2个数据用空格隔开。例如：
 200.1 200.2
 200.3 200.4

输出：
 输出两个点之间的距离。例如：
 0.28284271247464315
*/
public class points{

	public static void main(String[] args){
        
		System.out.println("请输入两个点，每个点坐标用空格间隔："); 
        //获得两个点座标的输入
        Scanner input = new Scanner(System.in); 
        String point1 = input.nextLine();
        String point2 = input.nextLine();
        
        String[] point1_arr = point1.split(" ");
        String[] point2_arr = point2.split(" ");
        if(point1_arr.length != 2 || point2_arr.length != 2){
        	System.out.println("每个点坐标应为两个参数，并以空格隔开");
        }
        else{
        	try{ 
        		double point1_x = Double.valueOf(point1_arr[0]); 
        		double point1_y = Double.valueOf(point1_arr[1]); 
        		double point2_x = Double.valueOf(point2_arr[0]); 
        		double point2_y = Double.valueOf(point2_arr[1]);
        		
        		if(point1_x <= 0 || point1_y <= 0 || point2_x <=0 || point2_y <= 0){
        			System.out.println("请输入正数");
        		}
        		else{
        			MyPoint m_point1 = new MyPoint(point1_x, point1_y);
        			MyPoint m_point2 = new MyPoint(point2_x, point2_y);
        			System.out.println(m_point1.getD(m_point2));
        		}
        	} catch (NumberFormatException e) {  
        		System.out.println("请输入数字");
        	} 
        }
 
	}
}

class MyPoint{
	private double x = 0.0;
	private double y = 0.0;
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public MyPoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @param point 
	 * @return 两点间距离
	 */
	public double getD(MyPoint point){
		double x2 = Math.pow(point.getX()-this.getX(), 2);
		double y2 = Math.pow(point.getY()-this.getY(), 2);
		return Math.sqrt(x2+y2);
	}
}

