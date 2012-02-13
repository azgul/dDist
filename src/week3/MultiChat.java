package week3;

import java.net.*;
import multicast.*;


/**
 *
 * @author Martin
 */
public class MultiChat {
	InetAddress host;
			
	public void main(String[] args) throws UnknownHostException {
		if (args.length >= 1)
			
		else if (args.length == 0) 
			initServer();
	}
	
	public void initServer() throws UnknownHostException{
		host = InetAddress.getLocalHost();
		
		
	}
}
