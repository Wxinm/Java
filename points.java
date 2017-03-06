package edu.develop.homework;

import java.util.Scanner;

/**
 * @title ������������
 * @author ��ܰ��
 *
 */
/*
��дһ����ʾ��άƽ���ϵĵ����MyPoint����������������
��1������private�ĳ�Ա����x��y����ʾ���x��y���꣬����Ϊdouble
��2����������MyPoint�Ĺ��췽����һ�����췽����������������x��y�ĳ�ʼֵΪ0����һ�����췽��������������������Ϊx��y������Ϊdouble���������������ֱ���Ϊ��ʼx��y����
��3������һ��getD��������һ������ΪMyPoint�Ķ������������Ϊ���ص�ǰ����Ͳ������������������ľ��룬����ֵΪdouble����
��4����д���Ե�main����������getD����������֮��ľ���

 ���룺
 ����2�����ݣ� �ܹ�4����������ÿ2������һ�飬��ʾһ�����x��y���꣬ÿ�е�2�������ÿո���������磺
 200.1 200.2
 200.3 200.4

�����
 ���������֮��ľ��롣���磺
 0.28284271247464315
*/
public class points{

	public static void main(String[] args){
        
		System.out.println("�����������㣬ÿ���������ÿո�����"); 
        //������������������
        Scanner input = new Scanner(System.in); 
        String point1 = input.nextLine();
        String point2 = input.nextLine();
        
        String[] point1_arr = point1.split(" ");
        String[] point2_arr = point2.split(" ");
        if(point1_arr.length != 2 || point2_arr.length != 2){
        	System.out.println("ÿ��������ӦΪ�������������Կո����");
        }
        else{
        	try{ 
        		double point1_x = Double.valueOf(point1_arr[0]); 
        		double point1_y = Double.valueOf(point1_arr[1]); 
        		double point2_x = Double.valueOf(point2_arr[0]); 
        		double point2_y = Double.valueOf(point2_arr[1]);
        		
        		if(point1_x <= 0 || point1_y <= 0 || point2_x <=0 || point2_y <= 0){
        			System.out.println("����������");
        		}
        		else{
        			MyPoint m_point1 = new MyPoint(point1_x, point1_y);
        			MyPoint m_point2 = new MyPoint(point2_x, point2_y);
        			System.out.println(m_point1.getD(m_point2));
        		}
        	} catch (NumberFormatException e) {  
        		System.out.println("����������");
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
	 * @return ��������
	 */
	public double getD(MyPoint point){
		double x2 = Math.pow(point.getX()-this.getX(), 2);
		double y2 = Math.pow(point.getY()-this.getY(), 2);
		return Math.sqrt(x2+y2);
	}
}

