package replicated_calculator;

import multicastqueue.Timestamp;

/**
 * An event describing the leaving of a block of events which must be 
 * executed atomically.
 * 
 * @author Jesper Buus Nielsen, Arrhus University, 2011
 *
 */

public class ClientEventEndAtomic extends ClientEvent {

	public ClientEventEndAtomic(String clientName, long eventID, Timestamp t) {
		super(clientName,eventID,t);
	}
	
	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}
	
}
