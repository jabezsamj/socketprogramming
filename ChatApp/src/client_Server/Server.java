package client_Server;

import java.io.*;
import java.net.*;

public class Server 
{
  public static void main(String[] args) throws Exception
  {
	  Server serve = new Server();
	  serve.run();
  }
  
  public void run () throws Exception
  {
	  
	  ServerSocket serv_socket = new ServerSocket(444);
	  Socket sock = serv_socket.accept();
	  InputStreamReader inReader = new InputStreamReader(sock.getInputStream());
	  BufferedReader buffReader = new BufferedReader(inReader);
	  
	  String message = buffReader.readLine();
	  System.out.println(message);
	  
	  if(message != null)
	  {
		  PrintStream printStream = new PrintStream(sock.getOutputStream());
		  printStream.println("Message Recieved!");
	  }
	  
	 

  }
}


