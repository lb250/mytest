package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client3 {
	
	//�ͻ������̣߳�
		public static void main(String[] args) {
			System.out.println("�ͻ���3��");
			
			try {
				//���ӷ�����
				Socket socket = new Socket("127.0.0.1",10002);
				
				//�ӿ���̨���յ�������
				BufferedReader br;
				//��������˷��͵������
				PrintStream ps;
				//�ӷ������˷�����������
				BufferedReader brServer;
				
				//�ȵ�¼
				while(true) {
					System.out.println("�������û���:");
					//���մ��ն��������Ϣ
					br = new BufferedReader(new InputStreamReader(System.in));
					String line = br.readLine();
					//ƴ�ӵ�¼��ʽ
					String loginStr = ChatProtocol.LOGIN_FLAG + line + ChatProtocol.LOGIN_FLAG;
					
					//���͸�������
					ps = new PrintStream(socket.getOutputStream());
					ps.println(loginStr);
					
					//���շ������˷��صĽ��
					brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String result = brServer.readLine();
					
					//�жϵ�¼�Ľ��
					if(result.equals(ChatProtocol.SUCCESS)) {
						//�ɹ�
						System.out.println("��¼�ɹ�");
						break;
					}else {
						//ʧ��
						System.out.println("�û����Ѵ��ڣ�����������");
					}
				}
				
				//�������̴߳���������˷���������
				new ClientThread(socket).start();
				
				System.out.println("���Կ�ʼ������");
				
				//���Ͻ����ն˵����룬���͸���������
				String line = null;
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				String name;
				int choose;
				
				while((line = br.readLine()) != null) {
					System.out.println("��ѡ���ͷ�ʽ:1.Ⱥ�� 2.˽��");
					choose = sc.nextInt();
					
					if(choose == 1) {
						//Ⱥ��
						ps.println(ChatProtocol.PUBLIC_FLAG+line+ChatProtocol.PUBLIC_FLAG);
					}else if(choose == 2) {
						//˽��
						System.out.println("�������û���:");
						name = sc.next();
						ps.println(ChatProtocol.PRIVATE_FLAG+name+ChatProtocol.SEPARATOR+line+ChatProtocol.PRIVATE_FLAG);
					}else {
						System.out.println("ѡ����Ч��������ѡ��");
					}
				}
						
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}

}
