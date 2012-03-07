/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessage;

/**
 *
 * @author larss
 */
public abstract class AbstractLamportMessage extends MulticastMessage implements LamportMessage {
	private double clock;
	public boolean isBacklog = false;
	
	public AbstractLamportMessage(InetSocketAddress addr){
		super(addr);
	}
	
	public double getClock(){
		int len = Integer.toString(getSender().hashCode()).length();
		
		return clock+(getSender().hashCode() / Math.pow(10, len));
	}
	
	public void setClock(double clock){
		this.clock = clock;
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
