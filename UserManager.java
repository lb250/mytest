package test;

import java.net.Socket;
import java.util.*;

public class UserManager {
	
	//保存所有用户信息
	private Map<String,Socket> users = new HashMap<> ();
	
	//判断用户是否已经登录
	public boolean isLogined(String name) {
		//遍历整个数组，查看是否有重名
		for(String key:users.keySet()) {
			if(key.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	//保存当前登录的用户信息
	public void save(String name,Socket socket) {
		users.put(name, socket);
	}
	
	//通过用户名获得对应的socket
	public Socket getSocketByName(String name) {
		return users.get(name);
	}
	
	//通过socket获得当前用户名
	public String getNamgeBySocket(Socket socket) {
		for(String key:users.keySet()) {
			if(socket == users.get(key)) {
				return key;
			}
		}
		return null;
	}
	
	//获取所有人的socket
	public synchronized Collection<Socket> getAllUsers(){
		return users.values();
	}

}
