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
public class ChatMessage extends LamportMulticastMessage {
	String message;
	
	public ChatMessage(InetSocketAddress sender, String message, int clock){
		super(sender, clock);
		this.message = message;
	}
	
	@Override
	public String toString(){
		return String.format("%s: %s", getSender().getHostName(), message);
	}
}
