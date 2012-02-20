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
public class JoinRelayMessage extends LamportMulticastMessage{
	private InetSocketAddress addressOfJoiner;
	
	public JoinRelayMessage(InetSocketAddress sender, InetSocketAddress joiner, int clock) {
	    super(sender, clock);
	    addressOfJoiner = joiner;
	}
	
	@Override
	public String toString(){
		return String.format("[%s has joined the chat!]", getAddressOfJoiner());
	}
	
	public InetSocketAddress getAddressOfJoiner(){
		return addressOfJoiner;
	}
}
