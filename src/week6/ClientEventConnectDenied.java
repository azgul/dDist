/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

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
	public ClientEventConnectDenied(String clientName, long eventID) {
		super(clientName,eventID);
	}

	@Override
	public void accept(ClientEventVisitor visitor) {
		// Do nothing
	}
	
}
