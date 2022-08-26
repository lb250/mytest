package test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
	
	//用于保存每一个用户对应的用户名和socket
	public static UserManager manager = new UserManager();
	
	//服务器主线程：设定服务器的监听端口，并保持监听
	public static void main(String[] args) {
		
		try {
			//1.创建ServerSocket 端口号：
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(10002);
			System.out.println("服务器开启成功");
			while(true) {
				//监听所有用来连接的客户端
				Socket socket = ss.accept();
				
				//将这个socket交给子线程处理 
				new ServerThread(socket).start();
			}			
		}catch(IOException e) {
			System.out.println("服务器开启失败");
			e.printStackTrace();
		}
			
	}
	
}

