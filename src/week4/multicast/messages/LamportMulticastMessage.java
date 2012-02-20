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
public class LamportMulticastMessage extends MulticastMessage{
	private int clock;
	
	public LamportMulticastMessage(InetSocketAddress addr, int clock){
		super(addr);
		this.clock = clock;
	}
	
	public int getClock(){ return clock; }
}
