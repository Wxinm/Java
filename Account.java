package edu.develop.account;

/**
 * @title 会计科目类
 * @author 王馨漫
 * 
 */
public class Account{
	
	private String code;
	private String name;
	//默认根节点为“0”
	private String p_code = "0";
	//level为0 代表一级科目，以此类推
	private int level = 0;
	
	public Account(String code, String name, String p_code, int level){
		this.code = code;
		this.name = name;
		this.p_code = p_code;
		this.level = level;
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
	
	public String toString(){
		return String.format("%-15s%-25s%-10s%d",
				this.code, this.name, this.p_code, this.level+1);
	}

}