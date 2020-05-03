/*
* Andre Hamm
* Troy Tully 
* Connection Runnable Class 
* Chat Server Computer Networks 
*/

import java.net.*;
import java.nio.file.Files;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.time.Instant;
import java.util.HashMap;
import java.util.Vector;

public class Connection implements Runnable
{
	private Socket client;
	private HashMap<String, Socket> connectionDict;
	private Vector<String> messages = null;
	private String username = null;
	
	public Connection(Socket client, HashMap<String, Socket> connectionDict, Vector messages) {
		this.connectionDict = connectionDict;
		this.messages = messages;
		this.client = client;
	}

	// server to client reponses
	// successful join
	private byte[] response201(){
		String header = ("status: 201"+ "\r\n" +
		"date: " + Instant.now().toString()+ "\r\n" +
		"\r\n\r\n");
		return (header.getBytes());
	}

	// unsuccessful (user already exists)
	private byte[] response401() {
		String header = ("status: 401"+ "\r\n" +
		"date: " + Instant.now().toString()+ "\r\n" +
		"\r\n\r\n");
		return (header.getBytes());
	}

	// client to server requests 
	// bad join request
	private byte[] request200(String[] lines) {
		this.username = lines[1].substring(1,lines[1].length()-1 );
		System.out.println(this.connectionDict.containsKey(this.username));

		if (this.connectionDict.containsKey(this.username)) {
			
			System.out.println("User already there");
			 
			try{client.close();} catch (IOException ioe) {
				System.err.println(ioe);
			}
			return response401();
		}
		else {
			System.out.println(lines[1].substring(1,lines[1].length()-1 ) + " username Added");
			// this.username = lines[1].substring(1,lines[1].length()-1 );
			this.connectionDict.put(this.username, this.client);
			System.out.println(this.connectionDict.containsKey(this.username) + "<- should be true");
			System.out.println("Username Added succesfully");
			messages.add("status: 200" + "\r\n"+ "date: "+ Instant.now().toString() +"\r\n" + "from: Server" +"\r\n"+ this.username + " has entered the chat room" + "\r\n\r\n");
			return response201();
		}
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		// IO
		BufferedReader in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedReader(new InputStreamReader(this.client.getInputStream())); // input stream from sender client 
			out = new BufferedOutputStream(this.client.getOutputStream()); // output stream to receipient client
			
			String line = null;
			while ((line = in.readLine()) != null) {
				//System.out.println(line.toString() + "Status first line");
				if (line.equals("status: 200")) {
					String[] lines = new String[2];
					lines[0] = in.readLine();
					lines[1] = in.readLine();
					byte[] outArray = request200(lines);
					System.out.println(outArray.toString());
					out.write(outArray, 0, outArray.length);
					out.flush();
				}
				else if (line.equals("status: 202")) {
					String lines[] = new String[3];
					lines[0] = in.readLine();
					lines[1] = in.readLine();
					lines[2] = in.readLine();
					messages.add(line + "\r\n"+ lines[0]+"\r\n" + lines[1] +"\r\n"+ lines[2]+ "\r\n\r\n");
					//System.out.println("General message"+ messages.firstElement());
					System.out.println(line + "\r\n" + lines[0]+"\r\n" + lines[1] +"\r\n"+ lines[2]+ "\r\n\r\n");
				}
				else if (line.equals("status: 203")) {
					String lines[] = new String[4];
					lines[0] = in.readLine();
					lines[1] = in.readLine();
					lines[2] = in.readLine();
					lines[3] = in.readLine();
					messages.add(line + "\r\n"+ lines[0]+"\r\n" + lines[1] +"\r\n"+ lines[2]+"\r\n"+ lines[3]+ "\r\n\r\n");
					//System.out.println("PM message"+ messages.firstElement());
					System.out.println(line + "\r\n"+ lines[0]+"\r\n" + lines[1] +"\r\n"+ lines[2]+"\r\n"+ lines[3]+ "\r\n\r\n");
				}
			}
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
