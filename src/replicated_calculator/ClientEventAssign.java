package replicated_calculator;
import java.math.*;
import multicastqueue.Timestamp;

/**
 * An event describing the assignment of a variable.
 * 
 * @author Jesper Buus Nielsen, Arrhus University, 2011
 *
 */

public class ClientEventAssign extends ClientEvent {

	public final String var;
	public final BigInteger val;

	/**
	 * 
	 * @param var The variable to be assigned
	 * @param val The value to assign to the variable
	 */
	ClientEventAssign(String clientName, long eventID, String var, BigInteger val, Timestamp t) {
		super(clientName,eventID,t);
		this.var = var;
		this.val = val;
	}

	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}
	
}
