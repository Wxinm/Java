package edu.develop.accountser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.develop.accountser.Account;

/**
 * @title ��Ŀ��
 * @author ��ܰ��
 * @description �ļ�����->����map->�������л�->�����л�
 */
public class AccountSerialize{
	
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
	 * ��ȡ��Ŀ�ļ�(�ı�)
	 * @param filepath �ļ�Ŀ¼
	 * @param addlength ��Ŀ�����������
	 * @return map<code,Account>
	 */
	public static HashMap<String, Account> readAccount(String filepath, int[] addlength){
		HashMap<String, Account> map = new LinkedHashMap<String, Account>();
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
						map.clear();
						break;
					}
					//һ����Ŀ�����ڵ�codeΪ"0"
					else if(index == 0){
						ArrayList<Account> childnodes = new ArrayList<Account>();
						acc = new Account(arr[0], arr[1], "0", index, childnodes);
					}
					else{
						ArrayList<Account> childnodes = new ArrayList<Account>();
						String pcode = arr[0].substring(0, sum(index-1, addlength));
						acc = new Account(arr[0], arr[1], pcode, index, childnodes);
						
						Account parent = map.get(pcode);
						if(parent != null){
							parent.getChildnodes().add(acc);
						}
					}
					map.put(arr[0], acc);
				}
				
			}else{
				System.out.println("�Ҳ����ļ���");
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * ��ƿ�Ŀд���ļ����������л���
	 * @param filepath �ļ�Ŀ¼
	 * @param map ��Ŀ���νṹHashMap
	 */
	public static void saveAccountSer(String filepath, HashMap<String, Account> map){
		FileOutputStream fop = null;
		try {
			
			File file = new File(filepath);
			fop = new FileOutputStream(file);
			// if file does not exists, then create it
			if (!file.exists()){
				file.createNewFile();
			}
			
			ObjectOutputStream os = new ObjectOutputStream(fop);
			Account[] accs = new Account[map.size()];
			int i = 0;
			for(String key : map.keySet()){
				accs[i] = map.get(key);
				i++;
			}
		
			os.writeObject(accs);
	
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
	 * ��ȡ��Ŀ�ļ�(�������л�)
	 * @param filepath �ļ�Ŀ¼
	 * @param addlength ��Ŀ�����������
	 * @return ��Ŀ����������
	 */
	public static Account[] readAccountSer(String filepath){
		Account[] accs = null;
		try{
			FileInputStream fi = new FileInputStream(filepath);
			ObjectInputStream si = new ObjectInputStream(fi);
			accs = (Account[])si.readObject();
			si.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accs;
	}
	
	public static void main(String[] args){
		//��Ŀ�����������
		int[] addlength = {4,2,2};
		String filepath = "D:\\�̶���Ŀ.txt";
		String filepath_tree = "D:\\Accounts.ser";
		HashMap<String, Account> map = readAccount(filepath, addlength);
//		for(Account acc : list){
//			System.out.println(acc.toString());
//		}
		if(!map.isEmpty()){
			saveAccountSer(filepath_tree, map);
			System.out.println("-------------");
			Account[] accs = readAccountSer(filepath_tree);
			
			for(int i = 0; i < accs.length; i++){
				System.out.println(accs[i].toString());
			}
		}

	}
}