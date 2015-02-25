package org.craneprint.virtual_chickagent;

import org.craneprint.virtual_chickagent.files.PersistentFileManager;
import org.craneprint.virtual_chickagent.tcp.AgentTCPSocket;
import org.craneprint.virtual_chickagent.ui.TestJFrame;

public class LuanchAgent {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PersistentFileManager m = new PersistentFileManager();
		AgentTCPSocket a = new AgentTCPSocket(m);
		Thread t = new Thread(a);
		t.start();
		new TestJFrame(a);
	}

}
