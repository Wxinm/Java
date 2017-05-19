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
 * @title �½���ƿ�Ŀ�Ի���
 * @author ��ܰ��
 * @description �ϲ�:AccountGUI
 */
public class newAccDlg extends JDialog implements ActionListener{

	private AccountGUI parent = null;
	private JPanel contentPanel, buttonPane;
	private JLabel lb1, lb2, lb3;
	private JButton okBtn, cancelBtn;
	/*
	 * ��Ŀ���루����������Ϊ���ɱ༭���Զ�����ֵ��Ϊ����
	 * ��ɱ༭������INSERTǰӦ�ж��Ƿ���ϴ������,�Ƿ�������ͻ
	 * ���ļ����õڶ��ַ���
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

		lb1 = new JLabel("�ϼ���Ŀ:");
		lb1.setFont(new Font("����", Font.BOLD, 14));
		lb1.setBounds(10, 20, 80, 30);
		contentPanel.add(lb1);
		
		lb2 = new JLabel("��Ŀ����:");
		lb2.setFont(new Font("����", Font.BOLD, 14));
		lb2.setBounds(10, 55, 80, 30);
		contentPanel.add(lb2);
		
		lb3 = new JLabel("��Ŀ����:");
		lb3.setFont(new Font("����", Font.BOLD, 14));
		lb3.setBounds(10, 90, 80, 30);
		contentPanel.add(lb3);
		
		pNameText = new JTextArea();
		pNameText.setEditable(false);
		
		if(parent.getChosenAcc() == null){
			pNameText.setText("��ƿ�Ŀ");
		}else{
			pNameText.setText(parent.getChosenAcc().toString());
		}
		
		pNameText.setFont(new Font("����", Font.PLAIN, 14));
		pNameText.setBounds(100, 20, 180, 30);
		contentPanel.add(pNameText);
		
		codeText = new JTextArea();
		codeText.setFont(new Font("����", Font.PLAIN, 14));
		codeText.setBounds(100, 55, 180, 30);
		contentPanel.add(codeText);
		
		nameText = new JTextArea();
		nameText.setFont(new Font("����", Font.PLAIN, 14));
		nameText.setBounds(100, 90, 180, 30);
		contentPanel.add(nameText);
		
		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		okBtn = new JButton("ȷ��");
		okBtn.setActionCommand("OK");
		okBtn.addActionListener(this);
		buttonPane.add(okBtn);
		getRootPane().setDefaultButton(okBtn);
		
		cancelBtn = new JButton("ȡ��");
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
						"�������Ŀ���룡", 
						"��ʾ",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
				
			if(nameText.getText().equals("")){
				JOptionPane.showMessageDialog(newAccDlg.this,
						"�������Ŀ���ƣ�", 
						"��ʾ",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
				
			if(parent.getTreeMap().containsKey(codeText.getText())){
				JOptionPane.showMessageDialog(newAccDlg.this,
						"��Ŀ���루��������ͻ��", 
						"��ʾ",
						JOptionPane.WARNING_MESSAGE); 
				return;
			}
			//���ڵ�Ϊ����ƿ�Ŀ��
			if(parent.getChosenAcc() == null){
				if(codeText.getText().length() != parent.sum(0, parent.getAddLength())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"��Ŀ���볤��ӦΪ"+parent.getAddLength()[0]+"λ��", 
							"��ʾ",
							JOptionPane.WARNING_MESSAGE); 
					return;
				}
			}
			
			else{
				if(codeText.getText().length() != parent.sum(parent.getChosenAcc().getLevel()+1, parent.getAddLength())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"��Ŀ���볤��ӦΪ"+parent.sum(parent.getChosenAcc().getLevel()+1, parent.getAddLength())+"λ��", 
							"��ʾ",
							JOptionPane.WARNING_MESSAGE); 
					return;
				}
				else if(!codeText.getText().startsWith(parent.getChosenAcc().getCode())){
					JOptionPane.showMessageDialog(newAccDlg.this,
							"��Ŀ����ǰ"+parent.getChosenAcc().getCode().length()+"λӦ���ϼ���Ŀһ�£�", 
							"��ʾ",
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
