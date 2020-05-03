/**
 * This thread is passed a socket that it reads from. Whenever it gets input
 * it writes it to the ChatScreen text area using the displayMessage() method.
 */

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ReaderThread implements Runnable
{
	Socket server;
	BufferedReader fromServer;
	ChatScreen screen;
	

	public ReaderThread(Socket server, ChatScreen screen) {
		
		this.server = server;
		this.screen = screen;
	}


	public void run() {
		try {
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			boolean go = true;
			while (go) {
				String message = fromServer.readLine();
				System.out.println( message + " Reader Thread");
				String type = new String(message.substring(8, 11));	
				// System.out.println(type + "Type");
				if (type.equals("201")){
					System.out.println("YOU HAVE SUCCESFULLY JOINED MOTHAFUCKAH");
					fromServer.readLine(); //Read line to clear the incoming messages
					fromServer.readLine();
					fromServer.readLine();
					
				}
				else if (type.equals("202")){
					//System.out.println( message + " message Thread");	
					String date = new String( fromServer.readLine());
					String user = new String( fromServer.readLine());
					String content = new String( fromServer.readLine());
					fromServer.readLine();//Read line to clear the incoming messages
					message = date + ": " + user + ":" + content + "\r\n";
					screen.displayMessage(message);
					
					}
				else if (type.equals("203")){
					
					String date = new String( fromServer.readLine());
					String usernameFrom = new String(fromServer.readLine());
					String usernameto = new String(fromServer.readLine());
					String content = new String( fromServer.readLine());
					fromServer.readLine();//Read line to clear the incoming messages
					message = date + ": "  + usernameFrom + "to user" +usernameto + content + "\r\n";
					screen.displayMessage(message);
					}
				else if(type.equals("301")){
					String date = new String( fromServer.readLine());
					String content = new String( fromServer.readLine());
					fromServer.readLine();//Read line to clear the incoming messages
					message = date + ": "  + content + "\r\n";
					screen.displayMessage(message);
				}
				else if(type.equals("401")){
						fromServer.readLine();
						fromServer.readLine();
						// System.out.println("THAT USER NAME IS TAKEN MF");
						go = false;
					}
				else if(type.equals("404")){
					fromServer.readLine();//Read line to clear the incoming messages
					fromServer.readLine();
					fromServer.readLine();
					screen.displayMessage("THAT USER NAME IS DOESNT EXIST");
				}
				else{
					System.out.println(type + " parsed incorrectly");
				}

			}
		}
		catch (IOException ioe) { System.out.println(ioe); }

	}
}
