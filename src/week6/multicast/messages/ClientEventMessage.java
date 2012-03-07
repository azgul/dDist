/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6.multicast.messages;

import java.net.InetSocketAddress;
import replicated_calculator.ClientEvent;
import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ClientEventMessage extends AbstractLamportMessage {
	private double clock;
	public boolean isLocalMessage = false;
	private ClientEvent event;
	
	public ClientEventMessage(InetSocketAddress addr, ClientEvent e){
		super(addr);
		event = e;
	}
	
	public double getClock(){
		int len = Integer.toString(getSender().hashCode()).length();

		return clock+(getSender().hashCode() / Math.pow(10, len));
	}
	
	public void setClock(double clock){
		this.clock = clock;
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
		hash *= 23 * clock;
		
		return hash;
	}
}
