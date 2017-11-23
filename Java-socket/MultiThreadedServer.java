package servers;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class MultiThreadedServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public MultiThreadedServer(int port){
        this.serverPort = port;
    }

    public void run()
    {
        synchronized(this)
        {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped())
        {
            Socket clientSocket = null;
            try 
            {
                clientSocket = this.serverSocket.accept();
            } 
            catch (IOException e) 
            {
                if(isStopped()) 
                {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread( new WorkerRunnable(clientSocket, "Multithreaded Server", this.serverPort)).start();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() 
    {
        return this.isStopped;
    }

    public synchronized void stop()
    {
        this.isStopped = true;
        try 
        {
            this.serverSocket.close();
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Error closing server", e);
        }
    }
   
    // Server Socket code
    private void openServerSocket() {
        try 
        {
            this.serverSocket = new ServerSocket(this.serverPort);
            System.out.println("Server is running at port : " + this.serverPort);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Cannot open port", e);
        }
    }

}


