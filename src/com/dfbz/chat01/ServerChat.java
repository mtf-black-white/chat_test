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
	private JButton startBtn=new JButton("启动");
	private JButton stopBtn=new JButton("停止");
	
	private ServerSocket ss=null;
	private Socket s=null;
	
	private ArrayList<ClientConn> ccList = new ArrayList<ClientConn>();
	
	private boolean isStart=false;
	
	public ServerChat() {
		this.setTitle("服务器");
		this.add(serverTa,BorderLayout.CENTER);
		btnTool.add(startBtn);
		btnTool.add(stopBtn);
		this.add(btnTool,BorderLayout.SOUTH);
		
		this.setBounds(0, 0, 500, 500);
		if(isStart) {
			serverTa.append("服务器已经启动！"+"\n");
		}else {
			serverTa.append("服务器未能启动，请点击启动按钮\\n\\n"+"\n");
		}
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isStart=false;
			
					try  {
						if(ss!=null) {
							ss.close();
					}
					System.out.println("服务器断开!");
					serverTa.append("服务器断开");
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
				System.out.println("服务器断开!");
				serverTa.append("服务器断开");
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
					serverTa.append("服务器已经启动了！"+"\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			});
		this.setVisible(true);
			startServer();
	}
	//服务器启动方法
	public void startServer()  {
		try{try {
			ss=new ServerSocket(PORT);
			isStart=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//可以接受多个客户端的连接,让客户端的多条信息
		while (isStart) {
			Socket s = ss.accept();
			ccList.add(new ClientConn(s));
			System.out.println("一个客户端连接服务器" + s.getInetAddress() + "/" + s.getPort()+"\n");
			serverTa.append("一个客户端连接服务器" + s.getInetAddress() + "/" + s.getPort()+"\n");
		}
		
		}catch(SocketException e) {
			System.out.println("服务器中断");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reciveStr();	
	}
	//服务器停止的方法
	public void stopServer() {
		
	}
	//服务器接受数据方法应该用于多线程
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
	
	//这个对象是属于服务器端的一个连接对象
	class ClientConn implements Runnable{
		Socket s = null;
		public ClientConn(Socket s) {
			this.s = s;
			(new Thread(this)).start();
		}
		//同时接受客户端信息的
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				DataInputStream dis=new DataInputStream(s.getInputStream());
				while(isStart) {
					
					String str=dis.readUTF();
					
					System.out.println(s.getInetAddress()+"|"+s.getPort()+"说："+str+"\n");
					serverTa.append(s.getInetAddress()+"|"+s.getPort()+"说："+str+"\n");
					String strSend = s.getInetAddress()+"|"+s.getPort()+"说："+str+"\n";
					//需要遍历ccList，调用send方法,在客户端接受信息十多线程的接
					Iterator<ClientConn> it = ccList.iterator();
					while(it.hasNext()) {
						ClientConn o = it.next();
						o.send(strSend);
						}
				}
			} catch(SocketException e) {
				System.out.println("一个客户端下线了");
				serverTa.append("客户端下线：" +s.getInetAddress());
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//每个连接对象发送数据的方法
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
