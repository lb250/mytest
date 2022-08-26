package test;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

//���������̣߳�����ÿ���ͻ��˷���������
public class ServerThread extends Thread{
	private Socket socket;
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		//�ӿͻ��˷�����������
		BufferedReader br;
		//���͸��ͻ��˵������
		PrintStream ps;
		
		try {
			br = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());
			
			String line = null;
			while((line = br.readLine()) != null) {
				//�����ݴ��ݹ������жϴ��ݵ����� 1.��¼ 2.˽�� 3.Ⱥ��
				if(line.startsWith(ChatProtocol.LOGIN_FLAG) && line.endsWith(ChatProtocol.LOGIN_FLAG)) {
					//��¼
					
					//��ȡ�û���
					int signLength = ChatProtocol.LOGIN_FLAG.length();
					String name = line.substring(signLength,line.length()-signLength);
					System.out.println(name + "�����¼");
					
					//�ж�����û����Ƿ��½��
					if(Server.manager.isLogined(name)) {
						//��½����
						//���͸��ͻ��ˣ���ʾ��¼ʧ��
						ps.println(ChatProtocol.FAIL);
						System.out.println(name + "��¼ʧ��");
					}else {
						//û�е�½��
						//���浱ǰ��¼���û���Ϣ
						Server.manager.save(name, socket);
						//���͸��ͻ��ˣ���ʾ��½�ɹ�
						ps.println(ChatProtocol.SUCCESS);
						System.out.println(name + "��¼�ɹ�");
					}
					
				}else if(line.startsWith(ChatProtocol.PRIVATE_FLAG) && (line.endsWith(ChatProtocol.PRIVATE_FLAG))) {
					//˽��
					//��ȡ��������Ϣ
					int sighLength = ChatProtocol.PRIVATE_FLAG.length();
					String message = line.substring(sighLength,line.length()-sighLength);
					
					//�ָ�Ŀ���û�������Ϣ
					String[] items = message.split(ChatProtocol.SEPARATOR);
					String name = items[0];
					String realMessage = items[1];
					
					//ͨ���û���Ѱ�ҵ�Ŀ��socket
					Socket targetSocket = Server.manager.getSocketByName(name);
					
					//�ж�����û��Ƿ����
					if(targetSocket == null) {
						//�����ڣ����ͻ��˷�����Ϣ��ʾ
						ps.println("���û������ڣ�");						
					}else {
						//����
						//System.out.println(name + realMessage);
						
						//�õ���ǰ���û���
						String currentUserName = Server.manager.getNamgeBySocket(socket);
						
						//���͵�Ŀ���û�
						PrintStream mesPs = new PrintStream(targetSocket.getOutputStream());
						mesPs.println(currentUserName + "����˽��:" + realMessage);		
					}
							
				}else if(line.startsWith(ChatProtocol.PUBLIC_FLAG) && (line.endsWith(ChatProtocol.PUBLIC_FLAG))) {
					//Ⱥ��
					//��ȡ��������Ϣ
					int sighLength = ChatProtocol.PUBLIC_FLAG.length();
					String message = line.substring(sighLength,line.length()-sighLength);
					
					//���������û���Ϣ�������Ƿ�������
					//��ȡ�����û���socket
					Collection<Socket> sockets = Server.manager.getAllUsers();
					//��ȡ��ǰ���û���
					String currentUserName = Server.manager.getNamgeBySocket(socket);
					
					//������Ϣ
					for(Socket s:sockets) {
						PrintStream mesPs = new PrintStream(s.getOutputStream());
						mesPs.println(currentUserName+"����Ⱥ��:"+message);
					}
					
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
