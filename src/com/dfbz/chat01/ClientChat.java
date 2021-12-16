package com.dfbz.chat01;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.AncestorListener;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.*;

public class ClientChat extends JFrame{
	private JTextArea ta=new JTextArea(10,20);//ta��������ʾ��
	private JTextField tf=new JTextField(20);//tf�������
	private static final String CONNSTR="127.0.0.1";
	private static final int CONNPORT=8888;
	private Socket s=null;
	
	private DataOutputStream dos=null;
	//���߳�ʵ�ֶ���������Ŀ
	//1�������ͻ��˴��� Ҫ�����������ܱ༭��ta.setEditable(false);��
	//1.2�������ְ��س�������ta�� �� tf���������õ�tf�����ݣ�append��ta
	//2�� �����������˴��ڣ���ͻ������ӡ�
	private boolean isConn = false;
	
	public ClientChat() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		this.setTitle("�ͻ��˴���");
		this.add(ta,BorderLayout.CENTER);
		this.add(tf,BorderLayout.SOUTH);
		this.setBounds(300, 300,300, 400);
		tf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String strSend=tf.getText();
				//���͵�������
				
				send(strSend);
				if(strSend.trim().length()==0) {
					return;
				}
				tf.setText("");
				//ta.append(strSend+"\n");
				
			}
			});
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ta.setEditable(false);
		tf.requestFocus();//���۱�
		
		try {
			s=new Socket(CONNSTR,CONNPORT);
			//��ʶ���ӷ�����
			isConn = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setVisible(true);
		new Thread(new Receive()).start();//�������߳�
	}
	//���ͷ���
	public void send(String str) {
		try {
			dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//���ܷ��������̵߳��࣬ʵ����Runnable�ӿڵ�
	class Receive implements Runnable{

		@Override
		public void run() {
			try {
				while(isConn) {
					DataInputStream dis = new DataInputStream(s.getInputStream());
					String str=dis.readUTF();
					ta.append(str);
				}
			} catch(SocketException e) {
				System.out.println("������������ֹ");
				ta.append("������������ֹ \n");
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		
		ClientChat cc=new ClientChat();
		cc.init();
	}

}
