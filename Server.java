package test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
	
	//���ڱ���ÿһ���û���Ӧ���û�����socket
	public static UserManager manager = new UserManager();
	
	//���������̣߳��趨�������ļ����˿ڣ������ּ���
	public static void main(String[] args) {
		
		try {
			//1.����ServerSocket �˿ںţ�
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(10002);
			System.out.println("�����������ɹ�");
			while(true) {
				//���������������ӵĿͻ���
				Socket socket = ss.accept();
				
				//�����socket�������̴߳��� 
				new ServerThread(socket).start();
			}			
		}catch(IOException e) {
			System.out.println("����������ʧ��");
			e.printStackTrace();
		}
			
	}
	
}

