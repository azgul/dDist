package week3;

import java.net.*;
import multicast.MulticastQueueFifoOnly;


/**
 *
 * @author Martin
 */
public class MultiChat {
	InetAddress host;
	int port = 1337;
	public final MulticastQueueFifoOnly<Integer> queue = new MulticastQueueFifoOnly<Integer>();
			
	public void main(String[] args) throws UnknownHostException {
		if (args.length >= 1)
			return;
		else if (args.length == 0) 
			initServer();
	}
	
	public void initServer() throws UnknownHostException{
		host = InetAddress.getLocalHost();
		queue.createGroup(port, host);
	}
}
