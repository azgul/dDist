/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class LeaveGroupMessage extends LamportMulticastMessage{
	public LeaveGroupMessage(InetSocketAddress addr, int clock){
		super(addr, clock);
	}
	
	public String toString(){
		return String.format("[%s has left the group!]", getSender());
	}
}
