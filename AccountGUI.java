package edu.develop.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.awt.Font;

import edu.develop.accountser.*;

/**
 * @title 会计科目管理
 * @author 王馨漫
 * @description 含main函数
 * 关联代码文件: Account.java DatabaseConnection.java newAccDlg.java
 * 数据库文件：D:/homework.sqlite
 */
public class AccountGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JPanel dispPane;
	private JTree accTree;
	private JLabel lb1, lb2;
	private JTextArea codeText, nameText;
	private JButton saveBtn;
	private JToolBar toolBar;
	private JButton newBtn, delBtn;

	private Connection conn = null;		//定义数据库连接对象
	private PreparedStatement pstmt = null ;	//定义数据库操作对象
	private int[] addlength = {4,2,2};  //科目代码规则数组
	private HashMap<String, Account> treemap = null;
	private Account chosenAcc = null;
	private Account newAcc = null;
	public boolean isNewSuccess = false;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				DatabaseConnection connection = null;
				try {
					connection = new DatabaseConnection();
					AccountGUI frame = new AccountGUI(connection.getConnection());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} 
//				finally{
//					if(conn != null)
//						try {
//							conn.close();
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public AccountGUI(Connection conn) throws SQLException {
		this.conn = conn;
		//因涉及批量删除，故设置autocommint为false,防止脏数据
		this.conn.setAutoCommit(false);
		
		DefaultMutableTreeNode root = treeInit();
		initialize(root);
		
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
	public int sum(int index, int[] addlength){
		int ret = 0;
		if(index > addlength.length)
			ret = -1;
		else{
			for(int i=0; i<=index; i++)
				ret += addlength[i];
		}
		return ret;
	}
	
	private DefaultMutableTreeNode treeInit(){
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("会计科目");
		try {
			this.treemap = new LinkedHashMap<String, Account>();
			String sql = "SELECT id, name FROM accounts ORDER BY id";
			this.pstmt = this.conn.prepareStatement(sql);
			ResultSet rs = this.pstmt.executeQuery();
			this.conn.commit();		//手动提交
			
			//形成树形数据结构HashMap
			while(rs.next()){
				String id = rs.getString(1);
				String name = rs.getString(2);
				Account acc = null;
				//科目级别
				int index = getAccountLevel(id.length(), addlength);
				
				if(index == -1){
					System.out.println("科目代码："+id+"超出科目代码规则数组");
					this.treemap.clear();
					break;
				}
				//一级科目，父节点code为"0"
				else if(index == 0){
					ArrayList<Account> childnodes = new ArrayList<Account>();
					acc = new Account(id, name, "0", index, childnodes);
					//numOfTop++;
				}
				else{
					ArrayList<Account> childnodes = new ArrayList<Account>();
					String pcode = id.substring(0, sum(index-1, addlength));
					acc = new Account(id, name, pcode, index, childnodes);
					
					Account parent = this.treemap.get(pcode);
					if(parent != null){
						parent.getChildnodes().add(acc);
					}
				}
				this.treemap.put(id, acc);
			}
			this.pstmt.close();
			
			//构造树形JTree结构
			for(String key : treemap.keySet()){
				Account acc = treemap.get(key);
				if(acc.getLevel() == 0){
					DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(acc);
					root.add(curNode);
					genTree(curNode,key);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return root;
		
	}
	
	/**
	 * 迭代生成树形结构
	 * @param curNode
	 * @param key
	 */
	private void genTree(DefaultMutableTreeNode curNode, String key){
		Account acc = treemap.get(key);
		if(acc.getChildnodes() != null && acc.getChildnodes().size() != 0){
			for(Account child : acc.getChildnodes()){
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
				curNode.add(childNode);
				genTree(childNode, child.getCode());
			}
		}
		
	}
	
	/**
	 * 获取级联子节点
	 * @param pCode
	 * @param codes
	 * @return
	 */
	private ArrayList<String> getChildCodes(String pCode, ArrayList<String> codes){		
		if(treemap.get(pCode).getChildnodes().size() != 0){
			for(Account child : treemap.get(pCode).getChildnodes()){
				this.getChildCodes(child.getCode(), codes);
			}
		}
		codes.add(pCode);
		
		return codes;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(DefaultMutableTreeNode root) {
		this.setTitle("会计科目");
		this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.2);
		
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		dispPane = new JPanel();
		dispPane.setLayout(null);
		splitPane.setRightComponent(dispPane);
		
		lb1 = new JLabel("科目代码：");
		lb1.setFont(new Font("宋体", Font.BOLD, 18));
		lb1.setBounds(20, 60, 100, 30);
		dispPane.add(lb1);
		
		lb2 = new JLabel("科目名称：");
		lb2.setFont(new Font("宋体", Font.BOLD, 18));
		lb2.setBounds(20, 110, 100, 30);
		dispPane.add(lb2);
		
		saveBtn = new JButton("保存");
		saveBtn.setBackground(UIManager.getColor("Button.background"));
		saveBtn.setFont(new Font("宋体", Font.PLAIN, 18));
		saveBtn.setBounds(36, 183, 100, 30);
		dispPane.add(saveBtn);
		saveBtn.addActionListener(this);
		
		codeText = new JTextArea();
		codeText.setEditable(false);
		codeText.setFont(new Font("宋体", Font.PLAIN, 16));
		codeText.setBounds(122, 60, 230, 30);
		dispPane.add(codeText);
		
		nameText = new JTextArea();
		nameText.setFont(new Font("宋体", Font.PLAIN, 16));
		nameText.setBounds(120, 110, 230, 30);
		dispPane.add(nameText);
		
		scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		accTree = new JTree(root);
		scrollPane.setViewportView(accTree);
		accTree.addTreeSelectionListener(onTreeNodeChanged);
		
		toolBar = new JToolBar();
		scrollPane.setColumnHeaderView(toolBar);
		
		newBtn = new JButton("新建");
		toolBar.add(newBtn);
		newBtn.addActionListener(this);
		
		delBtn = new JButton("删除");
		toolBar.add(delBtn);
		delBtn.addActionListener(this);
	}

	TreeSelectionListener onTreeNodeChanged = new TreeSelectionListener(){

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode chosenNode = (DefaultMutableTreeNode) accTree.getLastSelectedPathComponent();
			if(chosenNode != null){
				if(chosenNode.isRoot()){
					chosenAcc = null; 
					codeText.setText("");
					nameText.setText("");
				}
				else{
					chosenAcc = (Account)chosenNode.getUserObject();
					codeText.setText(chosenAcc.getCode());
					nameText.setText(chosenAcc.getName());
				}
				
			}
		}
		
	};
	
	private boolean doInsertAcc() throws SQLException{
		String sql = "INSERT INTO accounts VALUES(?,?)";
		this.pstmt = this.conn.prepareStatement(sql);						 
		this.pstmt.setString(1,newAcc.getCode());
		this.pstmt.setString(2,newAcc.getName());
		int ret = this.pstmt.executeUpdate();  //影响行数
		this.conn.commit();		//手动提交
		this.pstmt.close();
		
		if(ret != 0){
			if(chosenAcc != null){
				chosenAcc.getChildnodes().add(newAcc);
				treemap.get(chosenAcc.getCode()).getChildnodes().add(newAcc);
			}
			treemap.put(newAcc.getCode(), newAcc);
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode chosenNode = (DefaultMutableTreeNode) accTree.getLastSelectedPathComponent();
		if(e.getSource()==saveBtn){
			if(chosenNode == null || chosenNode.isRoot()){
				JOptionPane.showMessageDialog(splitPane, "请选择科目节点进行操作", "提示",JOptionPane.WARNING_MESSAGE);  
			}
			else{
				//Account acc = (Account)chosenNode.getUserObject();
				if(!this.chosenAcc.getName().equals(nameText.getText())){
					//update
					try {
						String sql = "UPDATE accounts SET name=? WHERE id=?";
						this.pstmt = this.conn.prepareStatement(sql);						 
						this.pstmt.setString(1,nameText.getText());
						this.pstmt.setString(2,this.chosenAcc.getCode());
						int ret = this.pstmt.executeUpdate();  //影响行数
						this.conn.commit();
						this.pstmt.close();
						
						if(ret != 0){
							this.chosenAcc.setName(nameText.getText());
							this.treemap.replace(this.chosenAcc.getCode(), this.chosenAcc);
							chosenNode.setUserObject(this.chosenAcc);
							accTree.updateUI();
							JOptionPane.showMessageDialog(splitPane, "保存成功", "提示",JOptionPane.DEFAULT_OPTION);  
						}
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		}
		
		if(e.getSource()==delBtn){
			if(chosenNode == null)
				JOptionPane.showMessageDialog(splitPane, "请选择一个树节点", "提示",JOptionPane.WARNING_MESSAGE);  
			else{
				if(chosenNode.isRoot()){
					JOptionPane.showMessageDialog(splitPane, "禁止删除全部科目", "提示",JOptionPane.WARNING_MESSAGE);  
				}
				else{
					try {
						String sql = "DELETE FROM accounts WHERE id=?";
						this.pstmt = this.conn.prepareStatement(sql);
						
						ArrayList<String> codes = this.getChildCodes(chosenAcc.getCode(), new ArrayList<String>());
						for(String c : codes){
							this.pstmt.setString(1, c);
							this.pstmt.addBatch();
							treemap.remove(c);
						}
						if(chosenAcc.getPCode() != "0")
							treemap.get(chosenAcc.getPCode()).getChildnodes().remove(chosenAcc);
						
						int[] rets = this.pstmt.executeBatch();  
						this.conn.commit();
						
						chosenNode.removeAllChildren();
						chosenNode.removeFromParent();
						accTree.updateUI();
						
						JOptionPane.showMessageDialog(splitPane, "删除成功", "提示",JOptionPane.WARNING_MESSAGE);  
//						chosenAcc = null; 
//						codeText.setText("");
//						nameText.setText("");
//						chosenNode = null;
						
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
				}
			}
		}
		
		if(e.getSource()==newBtn){
			if(chosenNode == null)
				JOptionPane.showMessageDialog(splitPane, "请选择一个树节点", "提示",JOptionPane.WARNING_MESSAGE);  
			else{
				if(chosenNode.isRoot()){
					newAccDlg dlg = new newAccDlg(AccountGUI.this, "新建科目", true);
					dlg.setLocation(this.getX() + this.getWidth()/2 - dlg.getWidth()/2, this.getY() +this.getHeight()/2 - dlg.getHeight()/2);
					dlg.show();
					if(this.isNewSuccess){
						try {
							if(this.doInsertAcc()){
								chosenNode.add(new DefaultMutableTreeNode(newAcc));
								accTree.updateUI();
								JOptionPane.showMessageDialog(splitPane,
										"新建成功", 
										"提示",
										JOptionPane.DEFAULT_OPTION);
							}
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						newAcc = null;
						this.isNewSuccess = false;
					}
					    
				}
				else{
					//level为0 代表一级科目，以此类推
					if(chosenAcc.getLevel()+1 >= this.addlength.length){
						JOptionPane.showMessageDialog(splitPane,
								"科目级别最多为"+String.valueOf(this.addlength.length)+"级\n更改'科目代码规则数组'定义级次", 
								"提示",
								JOptionPane.WARNING_MESSAGE); 
					}
					else{
						newAccDlg dlg = new newAccDlg(AccountGUI.this, "新建科目", true);
						dlg.setLocation(this.getX() + this.getWidth()/2 - dlg.getWidth()/2, this.getY() +this.getHeight()/2 - dlg.getHeight()/2);
						dlg.show();
						
						if(this.isNewSuccess){
							try {
								if(this.doInsertAcc()){
									
									chosenNode.add(new DefaultMutableTreeNode(newAcc));
									accTree.updateUI();
									JOptionPane.showMessageDialog(splitPane,
											"新建成功", 
											"提示",
											JOptionPane.DEFAULT_OPTION);
								}
								
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							newAcc = null;
							this.isNewSuccess = false;
						}
					}
				}
				
			}
				
		}
	}
	
	public Account getChosenAcc(){
		return this.chosenAcc;
	}
	
	public void setNewAcc(Account acc){
		this.newAcc = acc;
	}
	
	public int[] getAddLength(){
		return this.addlength;
	}
	
	public HashMap<String, Account> getTreeMap(){
		return this.treemap;
	}

}
