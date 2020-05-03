/*
* Andre Hamm
* Troy Tully 
* Chat Server Computer Networks 
*/

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.Vector;

public class MTChatServer {
    
    public static final int DEFAULT_PORT = 7331;
    
    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
		System.out.println("Server is listening at port 7331");
		ServerSocket sock = null;
		HashMap<String, Socket> connectionDict = new HashMap<String, Socket>();
		Vector<String> messages = new Vector<>();
		Thread threader = new Thread(new BroadcastThread(messages, connectionDict));
		threader.start();
		
		try {
			// threader.run();
			// establish the socket
			sock = new ServerSocket(DEFAULT_PORT);
			
			
			while (true) {
				/**
				 * now listen for connections
				 * and service the connection in a separate thread.
				 */
				// System.out.println("Running");
				Runnable task = new Connection(sock.accept(), connectionDict, messages);
				System.out.println("Socket Connection successful");
				exec.execute(task);
			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
		finally {
			if (sock != null)
				sock.close();
		}
	}

}