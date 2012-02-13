/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week3.multicast.messages;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class BacklogResponseMessage extends MulticastMessage {
	private ArrayList<MulticastMessage> backlog;
	
	public BacklogResponseMessage(InetSocketAddress addr, ArrayList<MulticastMessage> backlog){
		super(addr);
		this.backlog = backlog;
	}
	
	public ArrayList<MulticastMessage> getBacklog(){
		return backlog;
	}
	
	public String toString(){
		StringBuilder messages = new StringBuilder();
		for(MulticastMessage msg : backlog){
			messages.append(msg);
			messages.append("\n");
		}
		
		// Remove last linebreak
		messages.deleteCharAt(-1);
		
		return messages.toString();
	}
}
