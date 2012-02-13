package week3;

import java.io.IOException;
import java.net.*;
import java.util.*;
import multicast.*;


/**
 *
 * @author Martin
 */
public class MultiChat {
	private int port = 1337;
	private final MulticastChatQueue<String> queue = new MulticastChatQueue<String>();
	private ChatListener listener;
			
	public void main(String[] args) throws UnknownHostException, IOException {
		if (args.length >= 1)
			initClient(args[0]);
		else if (args.length == 0) 
			initServer();
		
		listener = new ChatListener(queue);
		listener.run();
		
		listen();
	}
	
	private void listen() {
		String msg;
		Scanner in = new Scanner(System.in);
		while (true) {
			if ((msg = in.nextLine()) != null) {
				if (msg.toLowerCase().equals("exit")) {
					listener.interrupt();
					queue.leaveGroup();
				} else {
					queue.put(msg);
				}
			}
		}
	}
	
	private void initClient(String host) throws IOException {
		queue.joinGroup(port, new InetSocketAddress(host, port), MulticastQueue.DeliveryGuarantee.NONE);
	}
	
	private void initServer() throws UnknownHostException, IOException{
		queue.createGroup(port, MulticastQueue.DeliveryGuarantee.FIFO);
	}
}
