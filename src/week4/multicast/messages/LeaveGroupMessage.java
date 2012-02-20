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
public class LeaveGroupMessage extends AbstractLamportMessage{
	public LeaveGroupMessage(InetSocketAddress addr){
		super(addr);
	}
	
	public String toString(){
		return String.format("[%s has left the group!]", getSender());
	}
}
