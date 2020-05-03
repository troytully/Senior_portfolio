/*
* Andre Hamm 
* Troy Tully 
* BroadcastThread
* Chat Server for Computer Networks  
*/

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.*;
//import jdk.nashorn.internal.ir.ReturnNode;
import java.util.*;
import java.time.*;

public class BroadcastThread implements Runnable
{
    private Vector<String> messages = null;
    private HashMap<String, Socket> connectionDict = null;
    private HashMap<String, BufferedOutputStream> outMap = new HashMap<String, BufferedOutputStream>();

    public BroadcastThread(Vector messages, HashMap connectionDict) {
        this.messages = messages;
        this.connectionDict = connectionDict;
    }

    // user not found
    private String response404() {
        return ("status: 404" + "\r\n" + "date: " + Instant.now().toString() +  "r\n\r\n");
    }

    private void userExited(String username) {
        BufferedOutputStream out = this.outMap.get(username);
        try {
            out.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
        outMap.remove(username);
        Socket sock = this.connectionDict.get(username);
        try {
            sock.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
        this.connectionDict.remove(username);
        this.outMap.remove(username);
        String userLeftMessage = "status: 301"+ "\r\n"+ "date: " + Instant.now().toString() + 
        "\r\n" + username + " has left the chat room." + "\r\n\r\n";
        // connectionDict.forEach((k, v) -> sendGeneralMessage(userLeftMessage, k));
        for(HashMap.Entry<String, Socket> pair: connectionDict.entrySet()){
            sendGeneralMessage(userLeftMessage, pair.getKey());
            System.out.println(pair.getKey());
        }
    }

    private void sendGeneralMessage(String full, String username) {
        System.out.println("INSIDE general message"+ full);
        BufferedOutputStream out = null;
        if (outMap.containsKey(username)) {
            out = this.outMap.get(username);
        }
        else {
            try {
                this.outMap.put(username, new BufferedOutputStream(this.connectionDict.get(username).getOutputStream()));
            System.out.println(this.connectionDict.get(username).getOutputStream());
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
            out = this.outMap.get(username);   
        }
        try {
            System.out.println("Writing Message: " + full);
            byte[] outArray = full.getBytes();
            out.write(outArray, 0, outArray.length);
            out.flush();
        }
        catch (SocketException ioe) {
            System.err.println(ioe);
            System.out.println("User Exiting " + username);
            userExited(username);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
            System.out.println("User Exiting " + username);
            userExited(username);
        }
    }

    private String sendPrivateMessage(String full, String username) {
        BufferedOutputStream out = null;
        if (outMap.containsKey(username)) {
            out = this.outMap.get(username);
        }
        else {
            return response404();
        }

        try {
            System.out.println("Writing Message: " + full);
            byte[] outArray = full.getBytes();
            out.write(outArray, 0, outArray.length);
            out.flush();
            return "OK";
        }
        catch (SocketException ioe) {
            System.err.println(ioe);
            System.out.println("User Exiting " + username);
            userExited(username);
            return response404();
        }
        catch (IOException ioe) {
            System.err.println(ioe);
            System.out.println("User Exiting " + username);
            userExited(username);
            return response404();
        }
    }

    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            // BufferedOutputStream out = null;
            // Iterator it = connectionDict.entrySet().iterator();

            try { Thread.sleep(100); } catch (InterruptedException ignore) { }

            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
          
             if (!messages.isEmpty()) {

                String newestMessage = messages.remove(0);
                System.out.println(newestMessage.substring(0, 11) + "END");
                if (newestMessage.substring(0, 11).equals("status: 202")) {
                   for(HashMap.Entry<String, Socket> pair: connectionDict.entrySet()){
                        sendGeneralMessage(newestMessage, pair.getKey());
                        System.out.println(pair.getKey());
                }
                System.out.println("we are running 202");
                    
                }
                else if (newestMessage.substring(0, 11).equals("status: 203") ){
                    String[] lines = newestMessage.split("\r\n");
                    
                    String from = lines[2].substring(7,lines[2].length()-1);
                    String username = lines[3].substring(4,lines[3].length()-1);
                   
                    String status = sendPrivateMessage(newestMessage, username);
                     
                    
                    BufferedOutputStream out = null;
                    if (!status.equals("OK")) {
                        
                        status = status + "from: [" + from+ "] \r\n\r\n"; 
                        if (this.outMap.containsKey(from)) {
                            out = this.outMap.get(from);
                        }
                        else {
                            try {
                                this.outMap.put(from, new BufferedOutputStream(this.connectionDict.get(from).getOutputStream()));
                            
                            } catch (IOException ioe) {
                                System.err.println(ioe);
                            }
                            out = this.outMap.get(from);   
                        }
                        System.out.println( out.equals(null) + "<- should be false");
                        byte[] outArray = status.getBytes();
                        try {
                            out.write(outArray, 0, outArray.length);
                            out.flush();
                        } catch (IOException ioe) {
                            System.err.println(ioe);
                            userExited(username);
                        }
                    }
                }
             }
        }
    }
} 