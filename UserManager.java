package test;

import java.net.Socket;
import java.util.*;

public class UserManager {
	
	//���������û���Ϣ
	private Map<String,Socket> users = new HashMap<> ();
	
	//�ж��û��Ƿ��Ѿ���¼
	public boolean isLogined(String name) {
		//�����������飬�鿴�Ƿ�������
		for(String key:users.keySet()) {
			if(key.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	//���浱ǰ��¼���û���Ϣ
	public void save(String name,Socket socket) {
		users.put(name, socket);
	}
	
	//ͨ���û�����ö�Ӧ��socket
	public Socket getSocketByName(String name) {
		return users.get(name);
	}
	
	//ͨ��socket��õ�ǰ�û���
	public String getNamgeBySocket(Socket socket) {
		for(String key:users.keySet()) {
			if(socket == users.get(key)) {
				return key;
			}
		}
		return null;
	}
	
	//��ȡ�����˵�socket
	public synchronized Collection<Socket> getAllUsers(){
		return users.values();
	}

}
