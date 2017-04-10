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
 * @author ��ܰ��
 * @description ���ı��ж�ȡ�ļ��������������ݿ���->�����ݿ��ж�ȡ��Ŀ�����γ����νṹ
 */
public class AccountSQLite{
	private Connection conn = null;		//�������ݿ����Ӷ���
	private PreparedStatement pstmt = null ;	//�������ݿ��������
	
	
	public AccountSQLite(Connection conn) throws SQLException{
		this.conn = conn;
		//���漰�������룬������autocommintΪfalse,��ֹ������
		this.conn.setAutoCommit(false);
	}
	
	/**
	 * ��ȡ��Ŀ����
	 * @param length ���볤��
	 * @param addlength ��Ŀ�����������
	 * @return ��Ŀ����0����һ����Ŀ���Դ�����
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
	 * ��ȡ��Ŀ�����ַ�������
	 * @param index ������
	 * @param addlength ��Ŀ�����������
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
	 * ���ı��ж�ȡ�ļ��������������ݿ���
	 * @param filepath �ļ�Ŀ¼
	 * @param addlength ��Ŀ�����������
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
					//�ָ���ɿ��Ǵ���
					String[] arr = line.split(" ");
					if(arr.length == 2){
						this.pstmt.setString(1,arr[0]);
						this.pstmt.setString(2,arr[1]);
						this.pstmt.addBatch();
					}
				}
				
				ret = this.pstmt.executeBatch();
				this.conn.commit();		//�ֶ��ύ
				this.pstmt.clearBatch();
				System.out.println("��Ŀ��Ǩ�����");
				
			}else{
				System.out.println("�Ҳ����ļ���");
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
	 * �����ݿ��ж�ȡ��Ŀ�����γ����νṹ
	 * @param addlength ��Ŀ�����������
	 * @return map<code,Account>
	 */
	public HashMap<String, Account> getAccountTree(int[] addlength){
		//��������map�ṹ������keyΪ��Ŀ����
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
				//��Ŀ����
				int index = getAccountLevel(id.length(), addlength);
				
				if(index == -1){
					System.out.println("��Ŀ���룺"+id+"������Ŀ�����������");
					treemap.clear();
					break;
				}
				//һ����Ŀ�����ڵ�codeΪ"0"
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
			String filepath = "D:\\�̶���Ŀ.txt";
			//1. ���ı��ж�ȡ�ļ��������������ݿ���
			sqlite.migrateAccount(filepath);
			
			//��Ŀ�����������
			int[] addlength = {4,2,2};
			
			//2. �����ݿ��ж�ȡ��Ŀ�����γ����νṹ
			HashMap<String, Account> map = sqlite.getAccountTree(addlength);
			
			//��ӡ
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