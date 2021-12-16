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
	private JTextArea ta=new JTextArea(10,20);//ta是聊天显示框
	private JTextField tf=new JTextField(20);//tf是输入框
	private static final String CONNSTR="127.0.0.1";
	private static final int CONNPORT=8888;
	private Socket s=null;
	
	private DataOutputStream dos=null;
	//多线程实现多人聊天项目
	//1、创建客户端窗口 要求文字区域不能编辑（ta.setEditable(false);）
	//1.2输入文字按回车发出在ta里 对 tf做监听，拿到tf的内容，append到ta
	//2、 创建服务器端窗口，与客户端连接。
	private boolean isConn = false;
	
	public ClientChat() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		this.setTitle("客户端窗口");
		this.add(ta,BorderLayout.CENTER);
		this.add(tf,BorderLayout.SOUTH);
		this.setBounds(300, 300,300, 400);
		tf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String strSend=tf.getText();
				//发送到服务器
				
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
		tf.requestFocus();//光标聚标
		
		try {
			s=new Socket(CONNSTR,CONNPORT);
			//标识连接服务器
			isConn = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setVisible(true);
		new Thread(new Receive()).start();//启动多线程
	}
	//发送方法
	public void send(String str) {
		try {
			dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//接受方法，多线程的类，实现了Runnable接口的
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
				System.out.println("服务器意外终止");
				ta.append("服务器意外终止 \n");
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
