package edu.develop.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import edu.develop.accountser.Account;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @title 新建会计科目对话框
 * @author 王馨漫
 * @description 上层:AccountGUI
 */
public class newAccDlg extends JDialog implements ActionListener{

	private AccountGUI parent = null;
	private JPanel contentPanel, buttonPane;
	private JLabel lb1, lb2, lb3;
	private JButton okBtn, cancelBtn;
	/*
	 * 科目代码（主键）设置为不可编辑，自动分配值更为合理
	 * 或可编辑，但是INSERT前应判断是否符合代码规则,是否主键冲突
	 * 本文件采用第二种方法
	 */
	private JTextArea pNameText, codeText, nameText;

	/**
	 * Create the dialog.
	 */
	public newAccDlg(AccountGUI frame, String title, boolean modal) {
		super(frame, title, modal);
		this.parent = frame;
		initialize();
			
	}
	
	private void initialize(){
		setBounds(100, 100, 400, 270);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lb1 = new JLabel("上级科目:");
		lb1.setFont(new Font("宋体", Font.BOLD, 14));
		lb1.setBounds(10, 20, 80, 30);
		contentPanel.add(lb1);
		
		lb2 = new JLabel("科目代码:");
		lb2.setFont(new Font("宋体", Font.BOLD, 14));
		lb2.setBounds(10, 55, 80, 30);
		contentPanel.add(lb2);
		
		lb3 = new JLabel("科目名称:");
		lb3.setFont(new Font("宋体", Font.BOLD, 14));
		lb3.setBounds(10, 90, 80, 30);
		contentPanel.add(lb3);
		
		pNameText = new JTextArea();
		pNameText.setEditable(false);
		
		if(parent.getChosenAcc() == null){
			pNameText.setText("会计科目");
		}else{
			pNameText.setText(parent.getChosenAcc().toString());
		}
		
		pNameText.setFont(new Font("宋体", Font.PLAIN, 14));
		pNameText.setBounds(100, 20, 180, 30);
		contentPanel.add(pNameText);
		
		codeText = new JTextArea();
		codeText.setFont(new Font("宋体", Font.PLAIN, 14));
		codeText.setBounds(100, 55, 180, 30);
		contentPanel.add(codeText);
		
		nameText = new JTextArea();
		nameText.setFont(new Font("宋体", Font.PLAIN, 14));
		nameText.setBounds(100, 90, 180, 30);
		contentPanel.add(nameText);
		
		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		okBtn = new JButton("确定");
		okBtn.setActionCommand("OK");
		okBtn.addActionListener(this);
		buttonPane.add(okBtn);
		getRootPane().setDefaultButton(okBtn);
		
		cancelBtn = new JButton("取消");
		cancelBtn.setActionCommand("Cancel");
		cancelBtn.addActionListener(this);
		buttonPane.add(cancelBtn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")){
			this.dispose();
		}
		
		if(e.getActionCommand().equals("OK")){
			if(codeText.getText().equals("")){
				JOptionPane.showMessageDialog(newAccDlg.this,
						"请输入科目代码！", 
						"提示",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
				
			if(nameText.getText().equals("")){
				JOptionPane.showMessageDialog(newAccDlg.this,
						"请输入科目名称！", 
						"提示",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
				
			if(parent.getTreeMap().containsKey(codeText.getText())){
				JOptionPane.showMessageDialog(newAccDlg.this,
						"科目代码（主键）冲突！", 
						"提示",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
			//父节点为“会计科目”
			if(parent.getChosenAcc() == null){
				if(codeText.getText().length() != parent.sum(0, parent.getAddLength())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"科目代码长度应为"+parent.getAddLength()[0]+"位！", 
							"提示",
							JOptionPane.WARNING_MESSAGE); 
					return;
				}
			}
			
			else{
				if(codeText.getText().length() != parent.sum(parent.getChosenAcc().getLevel()+1, parent.getAddLength())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"科目代码长度应为"+parent.sum(parent.getChosenAcc().getLevel()+1, parent.getAddLength())+"位！", 
							"提示",
							JOptionPane.WARNING_MESSAGE); 
					return;
				}
				else if(!codeText.getText().startsWith(parent.getChosenAcc().getCode())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"科目代码前"+parent.getChosenAcc().getCode().length()+"位应与上级科目一致！", 
							"提示",
							JOptionPane.WARNING_MESSAGE); 
					return;
				}
			}
			
			Account acc = new Account(codeText.getText(), 
					nameText.getText(), 
					parent.getChosenAcc()==null ? "0" : parent.getChosenAcc().getCode(),
					parent.getChosenAcc()==null ? 0 : parent.getChosenAcc().getLevel()+1,
					new ArrayList<Account>());
			parent.setNewAcc(acc);
			parent.isNewSuccess = true;
			this.dispose();
			
		}
		
	}

}
