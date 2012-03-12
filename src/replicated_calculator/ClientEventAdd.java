package replicated_calculator;

import multicastqueue.Timestamp;

/**
 * An event describing the addition of two variables and the storing of the result
 * in a third variable.
 * 
 * @author Jesper Buus Nielsen, Arrhus University, 2011
 *
 */
public class ClientEventAdd extends ClientEventWithThreeVariables {

	public ClientEventAdd(String clientName, long eventID, String left, String right, String res, Timestamp t) {
		super(clientName, eventID, left, right, res, t);
	}
		
	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}
	
}
