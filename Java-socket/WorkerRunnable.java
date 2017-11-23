package servers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**

 */

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	protected int serverPort = 0;
	protected String studentId = null;
	protected InetAddress ip = null;
		
        
	
	
	

	public WorkerRunnable(Socket clientSocket, String serverText, int serverPort) {
		this.clientSocket = clientSocket;
		this.serverPort = serverPort;
		this.serverText = serverText;
		try
		{
		this.ip = InetAddress.getLocalHost();


		}
		catch(UnknownHostException e)
		{
			 e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {

				InputStream input = clientSocket.getInputStream();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader clientInputBR = new BufferedReader(new InputStreamReader(input));
				String userInput = clientInputBR.readLine();
				
				while (userInput != null) {
					

					if (userInput.contains("HELO")) {


						out.println( "HELO text\n IP:" + ip + "\nPort:" + serverPort + "\nStudentID:" + studentId + "\n");
						userInput = null;
						// output.close();
						// input.close();

					}

				}
			} catch (IOException e) {
				// report exception somewhere.
				e.printStackTrace();
			}
		}
	}



}