package replicated_calculator;

/**
 * An event describing the entering of a block of events which must be 
 * executed atomically.
 * 
 * @author Jesper Buus Nielsen, Arrhus University, 2011
 *
 */

public class ClientEventBeginAtomic extends ClientEvent {

	public ClientEventBeginAtomic(String clientName, long eventID) {
		super(clientName,eventID);
	}

	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}

}
