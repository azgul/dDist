/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week3;

import java.net.InetSocketAddress;
import multicast.MulticastMessage;

/**
 *
 * @author larss
 */
public class MulticastChatMessage extends MulticastMessage {
	String message;
	
	public MulticastChatMessage(InetSocketAddress sender, String message){
		super(sender);
		this.message = message;
	}
	
	@Override
	public String toString(){
		return String.format("%s: %s", getSender().getHostName(), message);
	}
}
