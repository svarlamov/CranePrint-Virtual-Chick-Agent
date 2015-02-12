package org.craneprint.virtual_chickagent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import org.json.simple.JSONObject;

/*
 * Main.java
 * Basic JFrame with a basic JButton.
 * Created by: Joey
 * July 18, 2008
 */

public class TestJFrame extends JFrame {
	//private JTextField idField = new JTextField();
	
	public TestJFrame(AgentTCPSocket socket){
		/*
		 * JFrame.
		 */
		setSize(600, 100);//Size of JFrame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);//Sets if its visible.
		JPanel panel = new JPanel();
		add(panel);
		/*
		 * JButton.
		 */
		JButton startButton = new JButton("Request Job");//The JButton name.
		panel.add(startButton);//Add the button to the JFrame.
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Request Job");//what the button says when clicked.
				JSONObject obj = new JSONObject();
			    obj.put("type", RequestType.GET_NEW_JOB);
			    obj.put("printerId", 0);
			    try {
					String resp = socket.sendCommand(obj.toJSONString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});//Reads the action.
		
		JButton finishedButton = new JButton("TX Finished Call");//The JButton name.
		panel.add(finishedButton);//Add the button to the JFrame.
		finishedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Finished");//what the button says when clicked.
				JSONObject obj = new JSONObject();
			    obj.put("type", RequestType.JOB_COMPLETE);
			    // TODO: Actually get this value...
			    obj.put("printerId", 0);
			    try {
					String resp = socket.sendCommand(obj.toJSONString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});//Reads the action.
		//idField.setColumns(5);
		//panel.add(idField);
	}
}
