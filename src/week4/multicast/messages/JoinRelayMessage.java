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
public class JoinRelayMessage extends AbstractLamportMessage{
	private InetSocketAddress addressOfJoiner;
	
	public JoinRelayMessage(InetSocketAddress sender, InetSocketAddress joiner) {
	    super(sender);
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
