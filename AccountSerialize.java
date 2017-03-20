package edu.develop.accountser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.develop.accountser.Account;

/**
 * @title 科目树
 * @author 王馨漫
 * @description 文件读入->树形map->对象序列化->反序列化
 */
public class AccountSerialize{
	
	/**
	 * 获取科目级别
	 * @param length 代码长度
	 * @param addlength 科目代码规则数组
	 * @return 科目级别，0代表一级科目，以此类推
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
	 * 获取科目代码字符串长度
	 * @param index 数组标号
	 * @param addlength 科目代码规则数组
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
	 * 读取科目文件(文本)
	 * @param filepath 文件目录
	 * @param addlength 科目代码规则数组
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
					//分割符可考虑传参
					String[] arr = line.split(" ");
					Account acc = null;
					int index = getAccountLevel(arr[0].length(), addlength);
					
					if(index == -1){
						System.out.println("第"+linenum+"行科目代码，超出科目代码规则数组");
						map.clear();
						break;
					}
					//一级科目，父节点code为"0"
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
				System.out.println("找不到文件！");
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 会计科目写入文件（对象序列化）
	 * @param filepath 文件目录
	 * @param map 科目树形结构HashMap
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
			
			System.out.println("文件写入完毕");
			
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
	 * 读取科目文件(对象反序列化)
	 * @param filepath 文件目录
	 * @param addlength 科目代码规则数组
	 * @return 科目树对象数组
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
		//科目代码规则数组
		int[] addlength = {4,2,2};
		String filepath = "D:\\固定科目.txt";
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