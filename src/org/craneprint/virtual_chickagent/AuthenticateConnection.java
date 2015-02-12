package org.craneprint.virtual_chickagent;

public class AuthenticateConnection {
	private static final String pword = "password";
	
	public static boolean authenticatePassword(String p){
		if(p.equals(pword)){
			return true;
		}
		else{
			return false;
		}
	}
}