package edu.develop.accountser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title ��ƿ�Ŀ��
 * @author ��ܰ��
 * 
 */
public class Account implements Serializable{
	
	private String code;
	private String name;
	//Ĭ�ϸ��ڵ�Ϊ��0��
	private String p_code = "0";
	//levelΪ0 ����һ����Ŀ���Դ�����
	private int level = 0;
	private ArrayList<Account> childnodes = null;
	
	public Account(String code, String name, String p_code, int level, ArrayList<Account> childnodes){
		this.code = code;
		this.name = name;
		this.p_code = p_code;
		this.level = level;
		this.setChildnodes(childnodes);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPCode() {
		return p_code;
	}
	public void setPCode(String p_code) {
		this.p_code = p_code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<Account> getChildnodes() {
		return childnodes;
	}

	public void setChildnodes(ArrayList<Account> childnodes) {
		this.childnodes = childnodes;
	}
	
	@Override
	public String toString(){
		return this.code+"-"+this.name;
	}
	
	//���Դ�ӡ��ʾ���νṹ
//	public String toString(){
//		String str = "";
//		
//		str += this.code + "   ";
//		str += this.name + "   ";
//		str +="{";
//		
//		for(int i = 0; i < this.childnodes.size(); i++){
//			str += "[";
//			Account acc = this.childnodes.get(i);
//			str += acc.toString();
//			str += "]";
//			if(i != this.childnodes.size()-1)
//				str += ",";
//		}
//
//		str +="}";
//		
//		return str;
//	}

}