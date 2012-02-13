package week3;

import java.io.IOException;
import java.net.*;
import multicast.*;


/**
 *
 * @author Martin
 */
public class MultiChat {
	InetAddress host;
	int port = 1337;
	private final MulticastQueueFifoOnly<Integer> queue = new MulticastQueueFifoOnly<Integer>();
			
	public void main(String[] args) throws UnknownHostException, IOException {
		if (args.length >= 1)
			initClient(args[0]);
		else if (args.length == 0) 
			initServer();
	}
	
	private void initClient(String host) throws IOException {
		queue.joinGroup(port, new InetSocketAddress(host, port), MulticastQueue.DeliveryGuarantee.NONE);
	}
	
	private void initServer() throws UnknownHostException, IOException{
		host = InetAddress.getLocalHost();
		queue.createGroup(port, MulticastQueue.DeliveryGuarantee.FIFO);
	}
}
