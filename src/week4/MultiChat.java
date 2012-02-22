package week4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.swing.*;
import multicast.*;
//import ChatListener;
import week4.multicast.ChatQueue;


/**
 *
 * @author Martin
 */
public class MultiChat {
	private int port = 1337;
	private final ChatQueue queue = new ChatQueue();
	private ChatListener listener;
	private JTextField field;
	
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
		initMultiChat(host,port,port);
	}
	
	public MultiChat(String host, int serverPort){
		initMultiChat(host,port,serverPort);
	}
	
	public MultiChat(String host, int ownPort, int serverPort){
		initMultiChat(host, ownPort, serverPort);
	}
	
	public void initMultiChat(String host, int ownPort, int serverPort){
		try{
			initClient(host,ownPort,serverPort);
			start();
		}catch(BindException e){
			System.err.println("You cannot run multiple clients on the same computer, since the port ("+ownPort+") is already in use!");
			e.printStackTrace();
			System.exit(0);
		}catch(IOException e){
			System.err.println("IOException y0");
			e.printStackTrace();
		}
	}
	
	private void start(){
		JFrame frame = new JFrame("MultiChat");
		WindowListener closing = new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				queue.leaveGroup();
				listener.interrupt();
				System.exit(0);
			}
		};
		frame.addWindowListener(closing);
		
		JTextArea chatlog = new JTextArea(30,100);
		JScrollPane scroll = new JScrollPane(chatlog);
		chatlog.setEditable(false);
		
		field = new JTextField(80);
		field.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String msg = field.getText();
				field.setText("");
				if (msg.toLowerCase().equals("exit")) {
					queue.leaveGroup();
					listener.interrupt();
					System.exit(0);
				} else {
					queue.put(msg);
				}
			}
		});
		field.setToolTipText("Type message and press <Enter>");
		
		// This is not working properly at the moment!!
		JList list = new JList(); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		JScrollPane listScroll = new JScrollPane(list);
		listScroll.setPreferredSize(new Dimension(100,200));
		DefaultListModel model = new DefaultListModel();
		model.addElement("Me");
		list.setModel(model);
		queue.setUserList(model);
		
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(scroll, BorderLayout.CENTER);
		//content.add(listScroll, BorderLayout.WEST);
		content.add(field, BorderLayout.SOUTH);
		
		frame.setContentPane(content);
		frame.pack();
		frame.setVisible(true);
		
		listener = new ChatListener(queue, chatlog);
		listener.start();
	}
			
	public static void main(String[] args){
		MultiChat mc;
		if(args.length == 3){
			int oPort = Integer.parseInt(args[1]);
			int sPort = Integer.parseInt(args[2]);
			mc = new MultiChat(args[0],oPort,sPort);
		}
		else if(args.length == 2){
			int sPort = Integer.parseInt(args[1]);
			mc = new MultiChat(args[0],sPort);
		}
		else if (args.length == 1)
			mc = new MultiChat(args[0]);
		else
			mc = new MultiChat();
	}
	
	/**
	 * Init client with host and port
	 * @param String host of known peer
	 * @param int port of known peer
	 * @throws IOException 
	 */
	private void initClient(String host, int oPort, int sPort) throws IOException {
		System.out.println("Joining TrollFace-group~");
		queue.joinGroup(oPort, new InetSocketAddress(host, sPort), MulticastQueue.DeliveryGuarantee.NONE);
	}
	
	/**
	 * Init client with default port
	 * @param String host of known peer
	 * @throws IOException 
	 */
	private void initClient(String host) throws IOException {
		initClient(host,port,port);
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
