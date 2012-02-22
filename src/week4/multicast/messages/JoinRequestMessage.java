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
public class JoinRequestMessage extends AbstractLamportMessage {
	public JoinRequestMessage(InetSocketAddress myAddress){
		super(myAddress);
	}
	
	@Override
	public String toString(){
		return String.format("[%s has requested to join the chat!]", getSender());
	}
}
