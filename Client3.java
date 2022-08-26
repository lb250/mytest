package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client3 {
	
	//客户端主线程：
		public static void main(String[] args) {
			System.out.println("客户端3号");
			
			try {
				//连接服务器
				Socket socket = new Socket("127.0.0.1",10002);
				
				//从控制台接收的输入流
				BufferedReader br;
				//向服务器端发送的输出流
				PrintStream ps;
				//从服务器端发来的输入流
				BufferedReader brServer;
				
				//先登录
				while(true) {
					System.out.println("请输入用户名:");
					//接收从终端输入的信息
					br = new BufferedReader(new InputStreamReader(System.in));
					String line = br.readLine();
					//拼接登录格式
					String loginStr = ChatProtocol.LOGIN_FLAG + line + ChatProtocol.LOGIN_FLAG;
					
					//发送给服务器
					ps = new PrintStream(socket.getOutputStream());
					ps.println(loginStr);
					
					//接收服务器端返回的结果
					brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String result = brServer.readLine();
					
					//判断登录的结果
					if(result.equals(ChatProtocol.SUCCESS)) {
						//成功
						System.out.println("登录成功");
						break;
					}else {
						//失败
						System.out.println("用户名已存在，请重新输入");
					}
				}
				
				//开启子线程处理服务器端发来的数据
				new ClientThread(socket).start();
				
				System.out.println("可以开始聊天了");
				
				//不断接收终端的输入，发送给服务器端
				String line = null;
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				String name;
				int choose;
				
				while((line = br.readLine()) != null) {
					System.out.println("请选择发送方式:1.群发 2.私聊");
					choose = sc.nextInt();
					
					if(choose == 1) {
						//群发
						ps.println(ChatProtocol.PUBLIC_FLAG+line+ChatProtocol.PUBLIC_FLAG);
					}else if(choose == 2) {
						//私聊
						System.out.println("请输入用户名:");
						name = sc.next();
						ps.println(ChatProtocol.PRIVATE_FLAG+name+ChatProtocol.SEPARATOR+line+ChatProtocol.PRIVATE_FLAG);
					}else {
						System.out.println("选择无效，请重新选择");
					}
				}
						
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}

}
