package edu.develop.accountser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @title AccountSQLite
 * @author 王馨漫
 * @description 从文本中读取文件，并插入至数据库中->从数据库中读取科目，并形成树形结构
 */
public class AccountSQLite{
	private Connection conn = null;		//定义数据库连接对象
	private PreparedStatement pstmt = null ;	//定义数据库操作对象
	
	
	public AccountSQLite(Connection conn) throws SQLException{
		this.conn = conn;
		//因涉及批量插入，故设置autocommint为false,防止脏数据
		this.conn.setAutoCommit(false);
	}
	
	/**
	 * 获取科目级别
	 * @param length 代码长度
	 * @param addlength 科目代码规则数组
	 * @return 科目级别，0代表一级科目，以此类推
	 */
	private int getAccountLevel(int length, int[] addlength){
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
	private int sum(int index, int[] addlength){
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
	 * 从文本中读取文件，并插入至数据库中
	 * @param filepath 文件目录
	 * @param addlength 科目代码规则数组
	 */
	public int[] migrateAccount(String filepath){
		int[] ret = null;
		try{
			String encoding = "GBK";
			File file = new File(filepath);
			if(file.exists()){
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);
				BufferedReader br = new BufferedReader(read);
				
				String line = null;
				String sql = "INSERT OR IGNORE into accounts (id, name) VALUES (?, ?)";
				this.pstmt = this.conn.prepareStatement(sql);
				while((line = br.readLine()) != null){
					//分割符可考虑传参
					String[] arr = line.split(" ");
					if(arr.length == 2){
						this.pstmt.setString(1,arr[0]);
						this.pstmt.setString(2,arr[1]);
						this.pstmt.addBatch();
					}
				}
				
				ret = this.pstmt.executeBatch();
				this.conn.commit();		//手动提交
				this.pstmt.clearBatch();
				System.out.println("科目表迁移完毕");
				
			}else{
				System.out.println("找不到文件！");
			}
			
		} catch(Exception e){
			if(this.conn != null){
				try{
					this.conn.rollback();
				} catch(SQLException e1){
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			if(this.conn != null){
				try{
					this.pstmt.close();
//					this.conn.close();
				} catch(SQLException e2){
					e2.printStackTrace();
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 从数据库中读取科目，并形成树形结构
	 * @param addlength 科目代码规则数组
	 * @return map<code,Account>
	 */
	public HashMap<String, Account> getAccountTree(int[] addlength){
		//定义树形map结构，其中key为科目代码
		HashMap<String, Account> treemap = null;
		try {
			treemap = new LinkedHashMap<String, Account>();
			String sql = "SELECT id, name FROM accounts ORDER BY id";
			this.pstmt = this.conn.prepareStatement(sql);
			ResultSet rs = this.pstmt.executeQuery() ;

			while(rs.next()){
				String id = rs.getString(1);
				String name = rs.getString(2);
				Account acc = null;
				//科目级别
				int index = getAccountLevel(id.length(), addlength);
				
				if(index == -1){
					System.out.println("科目代码："+id+"超出科目代码规则数组");
					treemap.clear();
					break;
				}
				//一级科目，父节点code为"0"
				else if(index == 0){
					ArrayList<Account> childnodes = new ArrayList<Account>();
					acc = new Account(id, name, "0", index, childnodes);
				}
				else{
					ArrayList<Account> childnodes = new ArrayList<Account>();
					String pcode = id.substring(0, sum(index-1, addlength));
					acc = new Account(id, name, pcode, index, childnodes);
					
					Account parent = treemap.get(pcode);
					if(parent != null){
						parent.getChildnodes().add(acc);
					}
				}
				treemap.put(id, acc);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(this.conn != null){
				try{
					this.pstmt.close();
//					this.conn.close();
				} catch(SQLException e2){
					e2.printStackTrace();
				}
			}
		}
		
		return treemap;
	}
	
	public static void main(String[] args){
		DatabaseConnection conn = null;
		try {
			conn = new DatabaseConnection();
			AccountSQLite sqlite = new AccountSQLite(conn.getConnection());
			String filepath = "D:\\固定科目.txt";
			//1. 从文本中读取文件，并插入至数据库中
			sqlite.migrateAccount(filepath);
			
			//科目代码规则数组
			int[] addlength = {4,2,2};
			
			//2. 从数据库中读取科目，并形成树形结构
			HashMap<String, Account> map = sqlite.getAccountTree(addlength);
			
			//打印
			int i = 0;
			for(String key : map.keySet()){
				System.out.println(map.get(key).toString());
				i++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
}