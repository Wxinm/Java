package edu.develop.account;

import java.io.*;
import java.util.ArrayList;

/**
 * @title ��Ŀ��
 * @author ��ܰ��
 * @description �ļ�����->�������ݽṹ->����������
 * @question ������������
 */
public class AccountTree{
	
	/**
	 * ��ȡ��Ŀ����
	 * @param length ���볤��
	 * @param addlength ��Ŀ�����������
	 * @return ��Ŀ����0����һ����Ŀ���Դ�����
	 */
	private static int getAccountLevel(int length, int[] addlength){
		int ret = 0;
		int sum = 0;
		while(true){
			sum += addlength[ret];
			if(sum == length)
				break;
			if(ret+1 == addlength.length){
				ret = -1;
				break;
			}
			ret++;
		}
		return ret;
	}
	
	/**
	 * ��ȡ��Ŀ�����ַ�������
	 * @param index ������
	 * @param addlength ��Ŀ�����������
	 * @return
	 */
	private static int sum(int index, int[] addlength){
		int ret = 0;
		if(index > addlength.length)
			ret = -1;
		else{
			for(int i=0; i<=index; i++)
				ret += addlength[i];
		}
		return ret;
	}
	
	/**
	 * ��ƿ�Ŀд���ļ�
	 * @param filepath �ļ�Ŀ¼
	 * @param list ��Ŀ��
	 */
	public static void saveAccount(String filepath, ArrayList<Account> list){
		FileOutputStream fop = null;
		try {
			
			File file = new File(filepath);
			fop = new FileOutputStream(file);
			// if file does not exists, then create it
			if (!file.exists()){
				file.createNewFile();
			}
			
			for(Account acc : list){
				fop.write(acc.toString().getBytes());
				fop.write("\r\n".getBytes()); // д��һ������
			}
			fop.flush();
			fop.close();
			
			System.out.println("�ļ�д�����");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null){
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * ��ȡ��Ŀ�ļ�
	 * @param filepath �ļ�Ŀ¼
	 * @param addlength ��Ŀ�����������
	 * @return list
	 */
	public static ArrayList<Account> readAccount(String filepath, int[] addlength){
		
		ArrayList<Account> list = new ArrayList<Account>();
		try{
			String encoding = "GBK";
			File file = new File(filepath);
			if(file.exists()){
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);
				BufferedReader br = new BufferedReader(read);
				
				String line = null;
				int linenum = 0;
				while((line = br.readLine()) != null){
					linenum++;
					//�ָ���ɿ��Ǵ���
					String[] arr = line.split(" ");
					Account acc = null;
					int index = getAccountLevel(arr[0].length(), addlength);
					
					if(index == -1){
						System.out.println("��"+linenum+"�п�Ŀ���룬������Ŀ�����������");
						list.clear();;
						break;
					}
					else if(index == 0){
						//һ����Ŀ�����ڵ�codeΪ"0"
						acc = new Account(arr[0], arr[1], "0", index);
					}
					else{
						String pcode = arr[0].substring(0, sum(index-1, addlength));
						acc = new Account(arr[0], arr[1],pcode, index);
					}
					list.add(acc);
					
				}
				
			}else{
				System.out.println("�Ҳ����ļ���");
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public static void main(String[] args){
		//��Ŀ�����������
		int[] addlength = {4,2,2,2};
		String filepath = "D:\\�̶���Ŀ.txt";
		String filepath_tree = "D:\\��ƿ�Ŀ��.txt";
		ArrayList<Account> list = readAccount(filepath, addlength);
//		for(Account acc : list){
//			System.out.println(acc.toString());
//		}
		if(!list.isEmpty())
			saveAccount(filepath_tree, list);

	}
}