/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards 
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.net.UnknownHostException;

public class ChatScreen extends JFrame implements ActionListener, KeyListener
{
	private JButton sendButton;
	private JButton exitButton;
	private JTextField sendText;
	private JTextArea displayArea;

	// the default port
	private static final int PORT = 7331;
	// this could be replaced with an IP address or IP name
	// private static final String host = "146.86.199.85";
	// public static final String host = "localhost";
	private static String usernameGlobal = new String();
	private Socket server = null;
	private BufferedOutputStream toServer = null;
	private Thread readerThread = null;		
	 
	public ChatScreen(String socket, int PORT, String username){
		try{
		//Create Socket Connection
		server = new Socket(socket, PORT); 
		//Create output stream
		toServer = new BufferedOutputStream(server.getOutputStream());
	
		//Make user with username
		String userString = makeUser(username); //Make username with server
																								System.out.println(userString.toString());

		//write username to server
		toServer.write(userString.getBytes(), 0, userString.length()); 
		toServer.flush(); //flush pipe

		
		//Create reader thread
		readerThread= new Thread(new ReaderThread(server, this));
		readerThread.start();
		
	} catch (java.io.IOException ioe) {
		System.err.println(ioe);
	}
		/**
		 * a panel used for placing components
		 */
		JPanel p = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(30);
		sendButton = new JButton("Send");
		exitButton = new JButton("Exit");

		/**
		 * register the listeners for the different button clicks
		 */
        sendText.addKeyListener(this);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);

		/**
		 * add the components to the panel
		 */
		p.add(sendText);
		p.add(sendButton);
		p.add(exitButton);

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");

		/**
		 * add the text area for displaying output. Associate
		 * a scrollbar with this text area. Note we add the scrollpane
		 * to the container, not the text area
		 */
		displayArea = new JTextArea(15,40);
		displayArea.setEditable(false);
		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(displayArea);
		getContentPane().add(scrollPane,"Center");

		/**
		 * set the title and size of the frame
		 */
		setTitle(username + "\'s KickAss MF Chat Serv's up dawgie hang 10 nawmeen");
		pack();
 
		setVisible(true);
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

	}
	public void displayMessage(String message){
		//System.out.println(message + " message to screen");
		displayArea.append(message.toString());
	}
        
        /**
         * This gets the text the user entered and outputs it
         * in the display area.
         */
        public void displayText() {
			try{
				String line =  sendText.getText().trim();
				System.out.println(line.toString());
				String [] details = parseInput(line);
				String envelope = new String();
				envelope = makeMessage(details);
				// System.out.println(envelope.toString() + " Envelope");

				toServer.write(envelope.getBytes(), 0, envelope.length()); 
				toServer.flush(); //flush pipe

				 sendText.setText("");
				// sendText.requestFocus();
				//displayArea.append(line.toString() + "\n");
			} catch (java.io.IOException ioe) {
				System.err.println(ioe);
			}
        }


	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt)  {
		Object source = evt.getSource();

		if (source == sendButton) 
			displayText();
		else if (source == exitButton)
			System.exit(0);
	}
        
        /**
         * These methods responds to keystroke events and fulfills
         * the contract of the KeyListener interface.
         */
        
        /**
         * This is invoked when the user presses
         * the ENTER key.
         */
        public void keyPressed(KeyEvent e) { 
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                displayText();
        }
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
		public void keyTyped(KeyEvent e) {  }

	public String makeUser(String username){
		usernameGlobal = username;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");				
		LocalDateTime now = null;
		now = LocalDateTime.now(); 
		String date = dtf.format(now);
		String header = new String(
			"status: 200" + "\r\n"
			+ "date: " +  date + "\r\n"
			+ "["+username+"]" + "\r\n"
			+"\r\n\r\n");

			// toServer.write(envelope.getBytes(), 0, envelope.length()); 
			// toServer.flush(); //flush pipe
			return header;
		}


	public static String makeMessage(String [] input){

			String header = "";
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");				
			LocalDateTime now = null;
			now = LocalDateTime.now(); 
			String date = dtf.format(now);
			String content = input[2];
			String username = usernameGlobal;
			String type = new String(input[0]);
			// System.out.println(type.toString());

			if(type.equals("PM:{")){
				String usernameTo = input[1];
				header = ("status: 203"+ "\r\n" +
				"date: " + date+ "\r\n" +
				"from: ["+ username +"]" +"\r\n"+
				"to:["+ usernameTo +"]" +"\r\n" +
				"["+ content +"]"+ 
				"\r\n\r\n");
			}
			else if(type.equals("EXIT")){
				
				System.out.println("You have successfully left the chatRoom");
				header=null;
			}
			else{
				header = ("status: 202" + "\r\n"+
				"date: " + date + "\r\n"+
				"from: ["+ username + "]" +"\r\n"+
				"["+ content + "]"+
				"\r\n\r\n");
			}
			return header;
		}

		public static String [] parseInput(String message){
			String [] response = new String[3];
			String type = "";
			if(message.length() <= 4){
				 type = "general";
			}else{
				 type = message.substring(0,4);
			}

			if ( type.equals("PM:{")  ){
				String[] start = message.split("}",2);
       			String username = start[0];

				response[0] = "PM:{";
				response[1] = username.substring(4,username.length());
				response[2] = message.substring( username.length()+1, message.length() );
			
			}
			else if (type.equals("EXIT")){
				response[0] = "EXIT";
				response[1] = "";
			}
			else{
				response[0] = "general";
				response[2] = message.toString();
			}
		return response;
	}
	
	public static void main(String[] args) {

		JFrame win = new ChatScreen(args[0], PORT,args[1]);
		//ChatScreen chatScreen = new ChatScreen(args[0], PORT,args[1]);

		
	}
}
