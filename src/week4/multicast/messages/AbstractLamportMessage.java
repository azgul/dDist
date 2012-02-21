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
	private int clock;
	
	public AbstractLamportMessage(InetSocketAddress addr){
		super(addr);
	}
	
	public int getClock(){
		return clock;
	}
	
	public void setClock(int clock){
		this.clock = clock;
	}
	
	public boolean equals(Object other){
		if(!getClass().equals(other.getClass())){
			return false;
		}
		
		return clock == ((AbstractLamportMessage)other).clock;
	}
	
	@Override
	public int hashCode(){
		int hash = 1;
		
		hash *= 17 * getSender().hashCode();
		hash *= 13 * toString().hashCode();
		
		return hash;
	}
}
