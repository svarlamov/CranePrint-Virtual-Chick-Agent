package org.craneprint.virtual_chickagent.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import org.craneprint.virtual_chickagent.tcp.AgentTCPSocket;
import org.craneprint.virtual_chickagent.tcp.RequestType;
import org.json.simple.JSONObject;

public class TestJFrame extends JFrame {
	private JTextField idField = new JTextField();
	
	public TestJFrame(AgentTCPSocket socket){
		setSize(600, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		JPanel panel = new JPanel();
		add(panel);
		
		JButton startButton = new JButton("Request Job");//The JButton name.
		panel.add(startButton);//Add the button to the JFrame.
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JSONObject obj = new JSONObject();
			    obj.put("type", RequestType.GET_NEW_JOB);
			    obj.put("printerId", new Integer(idField.getText()));
			    try {
					String resp = socket.sendCommand(obj.toJSONString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton finishedButton = new JButton("TX Finished Call");
		panel.add(finishedButton);
		finishedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JSONObject obj = new JSONObject();
			    obj.put("type", RequestType.JOB_COMPLETE);
			    obj.put("printerId", new Integer(idField.getText()));
			    try {
					String resp = socket.sendCommand(obj.toJSONString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		idField.setColumns(5);
		panel.add(idField);
	}
}
