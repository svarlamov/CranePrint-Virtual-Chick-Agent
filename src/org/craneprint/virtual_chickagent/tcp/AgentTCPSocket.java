package org.craneprint.virtual_chickagent.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.craneprint.virtual_chickagent.config.Configs;
import org.craneprint.virtual_chickagent.files.HandleFile;
import org.craneprint.virtual_chickagent.files.PersistentFileManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AgentTCPSocket implements Runnable {
	private ServerSocket welcomeSocket;
	// TODO: Get these values from the settings!!!
	private static final String ip = "172.16.42.13";
	private static final int cranePort = 6770;
	private PersistentFileManager manager;
	private final HandleFile fileHandler = new HandleFile();
	// TODO: Get this folder dynamically/from settings
	private String fileFolder = "C:\\Users\\ckpcAdmin\\workspace\\CranePrint Virtual ChickAgent\\CranePrint Uploads";
	
	public AgentTCPSocket(PersistentFileManager m){
		manager = m;
	}

	@Override
	public void run() {
		String receivedText;
		try {
		welcomeSocket = new ServerSocket(Configs.getTCPPort());
		while(true){
				Socket connectionSocket = welcomeSocket.accept();             
				BufferedReader inFromCrane = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));             
				DataOutputStream outToCrane = new DataOutputStream(connectionSocket.getOutputStream());             
				receivedText = inFromCrane.readLine();
				if(receivedText.length() > 2){
					System.out.println("Received: " + receivedText);
					JSONObject jo = parseForObj(receivedText);
					int type = getType(jo);
					if(type == RequestType.FILE_CODE){
						// Actually write and add the file
						// TODO: With events and listeners or something, start printing that file!
						manager.setupNewJob(fileHandler.buildFileFromJSON(jo, fileFolder + "\\" + jo.get("user") + "\\"));
						// TODO: Respond with some more useful info, or at least make sure it actually succeeded
						outToCrane.writeBytes("{" + '"' + "response" + '"' + ":" + '"' + "success" + '"' + "}" + "\n");
					} 
					else if(type == RequestType.HAND_SHAKE_CODE){
						// Respond to the handshake
						JSONObject j = RespondHandShake.respond(jo);
						System.out.println(j.toJSONString());
						outToCrane.writeBytes(j.toJSONString() + "\n");
					}
					else if(type == RequestType.QUEUE_EMPTY){
						System.out.println("Queue is Empty");
						outToCrane.writeBytes("ok\n");
					}
					outToCrane.close();
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException pe){
			pe.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			welcomeSocket.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String sendCommand(String toSend) throws IOException{
		String resp; 
		Socket clientSocket = new Socket(ip, cranePort);  
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());   
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(toSend + "\n");   
		resp = inFromServer.readLine();
		// TODO: Parse Success Message
		System.out.println("FROM CRANE: " + resp); 
		clientSocket.close();
		return resp;
	}
	
	private JSONObject parseForObj(String arg0) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		return (JSONObject) jsonParser.parse(arg0);
	}
	
	private int getType(JSONObject j){
		int ret = (int)(long)j.get("type");
		return ret;
	}
}
