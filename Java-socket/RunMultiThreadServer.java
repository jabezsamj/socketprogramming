package servers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.Socket;




public class RunMultiThreadServer 
{
	
    public static void main (String args[]) 
    {
        	
    	MultiThreadedServer server = new MultiThreadedServer(9000);
    	new Thread(server).start();
        
 
    	//System.out.println("Stopping Server");
    	//server.stop();
  
    	
    }
}
