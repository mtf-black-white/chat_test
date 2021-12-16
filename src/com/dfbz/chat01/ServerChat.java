package com.dfbz.chat01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class ServerChat extends JFrame{
	private static final int PORT =8888;
	JTextArea serverTa=new JTextArea();
	private JPanel btnTool=new JPanel();
	private JButton startBtn=new JButton("����");
	private JButton stopBtn=new JButton("ֹͣ");
	
	private ServerSocket ss=null;
	private Socket s=null;
	
	private ArrayList<ClientConn> ccList = new ArrayList<ClientConn>();
	
	private boolean isStart=false;
	
	public ServerChat() {
		this.setTitle("������");
		this.add(serverTa,BorderLayout.CENTER);
		btnTool.add(startBtn);
		btnTool.add(stopBtn);
		this.add(btnTool,BorderLayout.SOUTH);
		
		this.setBounds(0, 0, 500, 500);
		if(isStart) {
			serverTa.append("�������Ѿ�������"+"\n");
		}else {
			serverTa.append("������δ������������������ť\\n\\n"+"\n");
		}
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isStart=false;
			
					try  {
						if(ss!=null) {
							ss.close();
					}
					System.out.println("�������Ͽ�!");
					serverTa.append("�������Ͽ�");
					System.exit(0);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		);
		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isStart=false;
				try  {
					if(ss!=null) {
						ss.close();
				}
				System.out.println("�������Ͽ�!");
				serverTa.append("�������Ͽ�");
				System.exit(0);
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("hhh");
				try {
					if(ss==null) {
						ss = new ServerSocket(PORT);
					}
					isStart = true;
					serverTa.append("�������Ѿ������ˣ�"+"\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			});
		this.setVisible(true);
			startServer();
	}
	//��������������
	public void startServer()  {
		try{try {
			ss=new ServerSocket(PORT);
			isStart=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//���Խ��ܶ���ͻ��˵�����,�ÿͻ��˵Ķ�����Ϣ
		while (isStart) {
			Socket s = ss.accept();
			ccList.add(new ClientConn(s));
			System.out.println("һ���ͻ������ӷ�����" + s.getInetAddress() + "/" + s.getPort()+"\n");
			serverTa.append("һ���ͻ������ӷ�����" + s.getInetAddress() + "/" + s.getPort()+"\n");
		}
		
		}catch(SocketException e) {
			System.out.println("�������ж�");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reciveStr();	
	}
	//������ֹͣ�ķ���
	public void stopServer() {
		
	}
	//�������������ݷ���Ӧ�����ڶ��߳�
	/*public void reciveStr() {
		try {
			dis=new DataInputStream(s.getInputStream());
			String str=dis.readUTF();
			System.out.println(str);
			serverTa.append(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	//������������ڷ������˵�һ�����Ӷ���
	class ClientConn implements Runnable{
		Socket s = null;
		public ClientConn(Socket s) {
			this.s = s;
			(new Thread(this)).start();
		}
		//ͬʱ���ܿͻ�����Ϣ��
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				DataInputStream dis=new DataInputStream(s.getInputStream());
				while(isStart) {
					
					String str=dis.readUTF();
					
					System.out.println(s.getInetAddress()+"|"+s.getPort()+"˵��"+str+"\n");
					serverTa.append(s.getInetAddress()+"|"+s.getPort()+"˵��"+str+"\n");
					String strSend = s.getInetAddress()+"|"+s.getPort()+"˵��"+str+"\n";
					//��Ҫ����ccList������send����,�ڿͻ��˽�����Ϣʮ���̵߳Ľ�
					Iterator<ClientConn> it = ccList.iterator();
					while(it.hasNext()) {
						ClientConn o = it.next();
						o.send(strSend);
						}
				}
			} catch(SocketException e) {
				System.out.println("һ���ͻ���������");
				serverTa.append("�ͻ������ߣ�" +s.getInetAddress());
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//ÿ�����Ӷ��������ݵķ���
		public void send(String str) {
			try {
				DataOutputStream dos = new DataOutputStream(this.s.getOutputStream());
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		ServerChat sc=new ServerChat();

	}

}
