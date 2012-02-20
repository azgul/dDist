/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessageJoin;

/**
 *
 * @author Lars Rasmussen
 */
public class JoinedMessage extends LamportMulticastMessage{
	public JoinedMessage(InetSocketAddress addr, int clock){
		super(addr, clock);
	}
	
	public String toString(){
		return String.format("[%s has joined the chat!]", getSender());
	}
}