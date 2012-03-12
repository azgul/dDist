/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

import multicastqueue.Timestamp;
import replicated_calculator.ClientEvent;
import replicated_calculator.ClientEventVisitor;

/**
 *
 * @author larss
 */
public class ClientEventConnectDenied extends ClientEvent {
	
	/**
	 * 
	 * @param clientName The name of the client, @see ClientEvent#clientName
	 * @param eventID The identifier of the event, @see ClientEvent#eventID
	 * @param clientAddress The address on which the client open the receiver end of its queue
	 */
	public ClientEventConnectDenied(String clientName, long eventID, Timestamp t) {
		super(clientName,eventID,t);
	}

	@Override
	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}
	
}
