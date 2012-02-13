/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week3.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessageJoin;

/**
 *
 * @author Lars Rasmussen
 */
public class JoinedMessage extends MulticastMessageJoin{
	public JoinedMessage(InetSocketAddress addr){
		super(addr);
	}
	
	public String toString(){
		return String.format("[%s has joined the chat!]", getSender());
	}
}
