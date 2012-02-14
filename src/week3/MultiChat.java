package week3;

import java.io.IOException;
import java.net.*;
import java.util.*;
import multicast.*;
import week3.ChatListener;
import week3.multicast.ChatQueue;


/**
 *
 * @author Martin
 */
public class MultiChat {
	private int port = 1337;
	private final ChatQueue queue = new ChatQueue();
	private ChatListener listener;
	
	public MultiChat(){
		try{
			initServer();
			start();
		}catch(UnknownHostException e){
			System.err.println("Fix y0 host kthxplz");
		}catch(IOException e){
			System.err.println("Server IOException y0");
			e.printStackTrace();
		}
	}
	
	public MultiChat(String host){
		try{
			initClient(host);
			start();
		}catch(IOException e){
			System.err.println("IOException y0");
			e.printStackTrace();
		}
	}
	
	private void start(){
		listener = new ChatListener(queue);
		listener.start();
		
		listen();
	}
			
	public static void main(String[] args){
		MultiChat mc;
		if (args.length >= 1)
			mc = new MultiChat(args[0]);
		else
			mc = new MultiChat();
	}
	
	private void listen() {
		System.out.println("Lets get this chat rollin'!");
		String msg;
		Scanner in = new Scanner(System.in);
		while (true) {
			if ((msg = in.nextLine()) != null) {
				if (msg.toLowerCase().equals("exit")) {
					queue.leaveGroup();
					listener.interrupt();
					break;
				} else {
					queue.put(msg);
				}
			}
		}
		System.exit(0);
	}
	
	private void initClient(String host) throws IOException {
		System.out.println("Joining TrollFace-group~");
		queue.joinGroup(port+4, new InetSocketAddress(host, port), MulticastQueue.DeliveryGuarantee.NONE);
	}
	
	private void initServer() throws UnknownHostException, IOException{
		System.out.println("Creating TrollFace-group~");
		
		queue.createGroup(port, MulticastQueue.DeliveryGuarantee.FIFO);
		
		queue.put(motd);
	}
	
	private String motd = 
			"\n░░░░░▄▄▄▄▀▀▀▀▀▀▀▀▄▄▄▄▄▄░░░░░░░░\n" +
			"░░░░░█░░░░▒▒▒▒▒▒▒▒▒▒▒▒░░▀▀▄░░░░\n" +
			"░░░░█░░░▒▒▒▒▒▒░░░░░░░░▒▒▒░░█░░░\n" +
			"░░░█░░░░░░▄██▀▄▄░░░░░▄▄▄░░░░█░░\n" +
			"░▄▀▒▄▄▄▒░█▀▀▀▀▄▄█░░░██▄▄█░░░░█░\n" +
			"█░▒█▒▄░▀▄▄▄▀░░░░░░░░█░░░▒▒▒▒▒░█\n" +
			"█░▒█░█▀▄▄░░░░░█▀░░░░▀▄░░▄▀▀▀▄▒█\n" +
			"░█░▀▄░█▄░█▀▄▄░▀░▀▀░▄▄▀░░░░█░░█░\n" +
			"░░█░░░▀▄▀█▄▄░█▀▀▀▄▄▄▄▀▀█▀██░█░░\n" +
			"░░░█░░░░██░░▀█▄▄▄█▄▄█▄████░█░░░\n" +
			"░░░░█░░░░▀▀▄░█░░░█░█▀██████░█░░\n" +
			"░░░░░▀▄░░░░░▀▀▄▄▄█▄█▄█▄█▄▀░░█░░\n" +
			"░░░░░░░▀▄▄░▒▒▒▒░░░░░░░░░░▒░░░█░\n" +
			"░░░░░░░░░░▀▀▄▄░▒▒▒▒▒▒▒▒▒▒░░░░█░\n" +
			"░░░░░░░░░░░░░░▀▄▄▄▄▄░░░░░░░░█░░";
}
