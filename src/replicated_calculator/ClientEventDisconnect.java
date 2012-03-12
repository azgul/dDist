package replicated_calculator;

import multicastqueue.Timestamp;

/**
 * An event describing a client disconnecting from a server.
 * 
 * @author Jesper Buus Nielsen, Arrhus University, 2011
 *
 */

public class ClientEventDisconnect extends ClientEvent {
	
	public ClientEventDisconnect(String clientName, long eventID, Timestamp t) {
		super(clientName,eventID,t);
	}
	
	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}
	
}
