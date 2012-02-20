/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;

/**
 *
 * @author Lars Rasmussen
 */
public class JoinedMessage extends AbstractLamportMessage{
	public JoinedMessage(InetSocketAddress addr){
		super(addr);
	}
	
	public String toString(){
		return String.format("[%s has joined the chat!]", getSender());
	}
}
