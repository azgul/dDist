/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6.multicast.messages;

import java.net.InetSocketAddress;
import multicastqueue.MulticastMessage;
import multicastqueue.Timestamp;
import replicated_calculator.ClientEvent;
import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ClientEventMessage extends MulticastMessage {
	private ClientEvent event;
	
	public ClientEventMessage(InetSocketAddress addr, Timestamp t, ClientEvent e){
		super(addr, t);
		event = e;
	}
	
	public ClientEvent getClientEvent(){
		return event;
	}
	
	/*public boolean equals(Object other){
		if(!getClass().equals(other.getClass())){
			return false;
		}
		
		return clock == ((AbstractLamportMessage)other).clock;
	}*/
	
	@Override
	public int hashCode(){
		int hash = 1;
		
		hash *= 17 * getSender().hashCode();
		hash *= 13 * toString().hashCode();
		hash *= 23;
		
		return hash;
	}
}
