package edu.develop.homework;

import java.util.Scanner;
/**
 * @title �������������ܳ�
 * @author ��ܰ��
 * 
 */

/*
����һ���򵥵ı�ʾ���ε�Rectangle�࣬��������������

��1������������Ա����height��width����ʾ���εĳ��Ϳ�����Ϊ���� 
��2������һ��getArea���������ؾ��ε���� 
��3������һ��getPerimeter���������ؾ��ε��ܳ� 
��4����main�����У����������2�������ֱ���Ϊ���εĳ��Ϳ�����getArea��getPermeter���������㲢���ؾ��ε�������ܳ�

 ���룺
 ����2�����������м��ÿո�������ֱ���Ϊ���εĳ��Ϳ����磺5 8

 �����
 ���2�����������м��ÿո�������ֱ��ʾ���ε�������ܳ������磺40 26
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
        
		System.out.println("�������������Ϳ��ÿո�����"); 
        //��ó��Ϳ������
        Scanner input=new Scanner(System.in); 
        String para=input.nextLine();
        
        String[] para_arr = para.split(" ");
        if(para_arr.length != 2){
        	System.out.println("��������������");
        }
        else{
        	try{ 
        		int height = Integer.parseInt(para_arr[0]); 
        		int width = Integer.parseInt(para_arr[1]); 
        		
        		if(height <= 0 || width <= 0){
        			System.out.println("������������");
        		}
        		else{
        			rectangle rec = new rectangle(height, width);
        			System.out.println(rec.getArea()+" "+rec.getPerimeter());
//        			System.out.println("���Ϊ��"+rec.getArea());
//        			System.out.println("�ܳ�Ϊ��"+rec.getPerimeter());
        		}
        	} catch (NumberFormatException e) {  
        		System.out.println("����������");
        	} 
        	
        }
 
	}
}