/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

import multicastqueue.Timestamp;
import replicated_calculator.ClientEventDisconnect;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ClientEventRemoteDisconnect extends ClientEventDisconnect {
	
	public ClientEventRemoteDisconnect(String clientName, long eventID, Timestamp t) {
		super(clientName,eventID,t);
	}
	
}
