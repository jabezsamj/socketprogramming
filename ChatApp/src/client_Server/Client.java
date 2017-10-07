package client_Server;

import java.io.*;
import java.net.*;


public class Client {
	
	public static void main(String[] args) throws Exception
	  {
		  Client client = new Client();
		  client.run();
	  }
	
	 public void run () throws Exception
	  {
		  Socket sock = new Socket("localhost", 444);
		  PrintStream printStream = new PrintStream(sock.getOutputStream());
		  printStream.println("Hello to Server");
		  
		  InputStreamReader inputReader = new InputStreamReader(sock.getInputStream());
		  BufferedReader buffReader = new BufferedReader(inputReader);
		  
		  String message = buffReader.readLine();
		  System.out.println(message);
	  }

}
