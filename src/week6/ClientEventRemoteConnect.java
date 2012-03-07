/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

import java.net.InetSocketAddress;
import replicated_calculator.ClientEventConnect;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ClientEventRemoteConnect extends ClientEventConnect {
	
	public ClientEventRemoteConnect(String clientName, long eventID, InetSocketAddress clientAddress){
		super(clientName, eventID, clientAddress);
	}
}
