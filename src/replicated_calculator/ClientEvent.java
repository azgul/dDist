package replicated_calculator;
import java.io.*;
import multicastqueue.Timestamp;

/**
 * Super class describing event created by clients. Carries the name of the client
 * creating the event and a unique identifier marking the event.
 * 
 * @author Jesper Buus Nielsen, Aarhus Universitet, 2011.
 *
 */

abstract public class ClientEvent implements Serializable {

	public final String clientName;
	public final long eventID;
	public Timestamp timestamp;
	
	/**
	 * 
	 * @param clientName The name of the client creating the event
	 * @param eventID A unique identifier  of the event, e.g. a sequence number
	 */
	public ClientEvent(String clientName, long eventID, Timestamp t) {
		this.clientName = clientName;
		this.eventID = eventID;
		timestamp = t;
	}
	
	/**
	 * A method which allows to visit this event.
	 * 
	 * @param visitor The visitor to visit this operation.
	 */
	abstract public void accept(ClientEventVisitor visitor);
}
