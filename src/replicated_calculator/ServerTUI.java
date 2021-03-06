package replicated_calculator;
import point_to_point_queue.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.math.BigInteger;
import week6.ServerReplicated;


/**
 * 
 * Rudimentary implementation of a Textual User Interface for a 
 * DistributedCalculator client.
 * 
 * @author Jesper Buus Nielsen, Aarhus University, 2012.
 *
 */

public class ServerTUI {

    synchronized public static void report(String str) {
	System.out.println(str);
    }
	
    public static void main(String[] args) {
	/**
	 * Here you would instantiation your own, more impressive server instead.
	 */
	ServerReplicated server = new ServerReplicated();
	
	try {
	    // For reading from standard input
	    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    
	    report("Exit: Gracefully logs out");
	    report("Crash: Makes the server crash.");
	    report("");
	    
	    /*
	     * Get the address of a server.
	     */
	    String serverAddress = null;
	    System.out.print("Enter address of another server (ENTER for standalone): ");
	    if ((s = stdin.readLine()) != null) {
		serverAddress = s;
	    } else {
		return;
	    }
	    
	    if (s.equals("")) {
			server.createGroup(Parameters.serverPortForServers, Parameters.serverPortForClients);
	    } else {
		//server.joinGroup(new InetSocketAddress(serverAddress,Parameters.serverPortForServers));
			server.joinGroup(Parameters.serverPortForServers, new InetSocketAddress(serverAddress, Parameters.serverPortForServers), Parameters.serverPortForClients);
	    }
	    
	    while ((s = stdin.readLine()) != null) { 
		if (s.equals("Crash")) {
		    System.exit(-1);
		} else if (s.equals("Exit")) {
		    server.leaveGroup();
		    break;
		} 
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println("Shutting down the server!");
    }
    
}
