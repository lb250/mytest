package test;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket socket;
	public ClientThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		//服务器端发来的数据
		BufferedReader br = null;		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = null;
			
			while((line = br.readLine()) != null) {
				//输出到控制台上
				System.out.println(line);
			}		
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				if(br != null) {
					br.close();
				}
				if(socket != null) {
					socket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
