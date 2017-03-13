package edu.develop.account;

import java.io.*;
import java.util.ArrayList;

/**
 * @title 科目树
 * @author 王馨漫
 * @description 文件读入->树形数据结构->保存至磁盘
 * @question 保存后对齐问题
 */
public class AccountTree{
	
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
	 * 会计科目写入文件
	 * @param filepath 文件目录
	 * @param list 科目树
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
				fop.write("\r\n".getBytes()); // 写入一个换行
			}
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
	 * 读取科目文件
	 * @param filepath 文件目录
	 * @param addlength 科目代码规则数组
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
					//分割符可考虑传参
					String[] arr = line.split(" ");
					Account acc = null;
					int index = getAccountLevel(arr[0].length(), addlength);
					
					if(index == -1){
						System.out.println("第"+linenum+"行科目代码，超出科目代码规则数组");
						list.clear();;
						break;
					}
					else if(index == 0){
						//一级科目，父节点code为"0"
						acc = new Account(arr[0], arr[1], "0", index);
					}
					else{
						String pcode = arr[0].substring(0, sum(index-1, addlength));
						acc = new Account(arr[0], arr[1],pcode, index);
					}
					list.add(acc);
					
				}
				
			}else{
				System.out.println("找不到文件！");
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public static void main(String[] args){
		//科目代码规则数组
		int[] addlength = {4,2,2,2};
		String filepath = "D:\\固定科目.txt";
		String filepath_tree = "D:\\会计科目树.txt";
		ArrayList<Account> list = readAccount(filepath, addlength);
//		for(Account acc : list){
//			System.out.println(acc.toString());
//		}
		if(!list.isEmpty())
			saveAccount(filepath_tree, list);

	}
}