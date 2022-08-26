package test;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

//服务器子线程：处理每个客户端发来的数据
public class ServerThread extends Thread{
	private Socket socket;
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		//从客户端发来的输入流
		BufferedReader br;
		//发送给客户端的输出流
		PrintStream ps;
		
		try {
			br = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());
			
			String line = null;
			while((line = br.readLine()) != null) {
				//有数据传递过来，判断传递的类型 1.登录 2.私聊 3.群发
				if(line.startsWith(ChatProtocol.LOGIN_FLAG) && line.endsWith(ChatProtocol.LOGIN_FLAG)) {
					//登录
					
					//截取用户名
					int signLength = ChatProtocol.LOGIN_FLAG.length();
					String name = line.substring(signLength,line.length()-signLength);
					System.out.println(name + "请求登录");
					
					//判断这个用户名是否登陆过
					if(Server.manager.isLogined(name)) {
						//登陆过了
						//发送给客户端，提示登录失败
						ps.println(ChatProtocol.FAIL);
						System.out.println(name + "登录失败");
					}else {
						//没有登陆过
						//保存当前登录的用户信息
						Server.manager.save(name, socket);
						//发送给客户端，提示登陆成功
						ps.println(ChatProtocol.SUCCESS);
						System.out.println(name + "登录成功");
					}
					
				}else if(line.startsWith(ChatProtocol.PRIVATE_FLAG) && (line.endsWith(ChatProtocol.PRIVATE_FLAG))) {
					//私聊
					//截取发来的信息
					int sighLength = ChatProtocol.PRIVATE_FLAG.length();
					String message = line.substring(sighLength,line.length()-sighLength);
					
					//分割目标用户名与消息
					String[] items = message.split(ChatProtocol.SEPARATOR);
					String name = items[0];
					String realMessage = items[1];
					
					//通过用户名寻找到目标socket
					Socket targetSocket = Server.manager.getSocketByName(name);
					
					//判断这个用户是否存在
					if(targetSocket == null) {
						//不存在，给客户端发送消息提示
						ps.println("该用户不存在！");						
					}else {
						//存在
						//System.out.println(name + realMessage);
						
						//得到当前的用户名
						String currentUserName = Server.manager.getNamgeBySocket(socket);
						
						//发送到目标用户
						PrintStream mesPs = new PrintStream(targetSocket.getOutputStream());
						mesPs.println(currentUserName + "发来私聊:" + realMessage);		
					}
							
				}else if(line.startsWith(ChatProtocol.PUBLIC_FLAG) && (line.endsWith(ChatProtocol.PUBLIC_FLAG))) {
					//群发
					//截取发来的信息
					int sighLength = ChatProtocol.PUBLIC_FLAG.length();
					String message = line.substring(sighLength,line.length()-sighLength);
					
					//遍历所有用户信息，向他们发送数据
					//获取所有用户的socket
					Collection<Socket> sockets = Server.manager.getAllUsers();
					//获取当前的用户名
					String currentUserName = Server.manager.getNamgeBySocket(socket);
					
					//发送消息
					for(Socket s:sockets) {
						PrintStream mesPs = new PrintStream(s.getOutputStream());
						mesPs.println(currentUserName+"发出群聊:"+message);
					}
					
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
