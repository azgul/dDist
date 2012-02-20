/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class BacklogMessage extends MulticastMessage {
	private ArrayList<MulticastMessage> backlog;
	
	public BacklogMessage(InetSocketAddress addr, ArrayList<MulticastMessage> backlog){
		super(addr);
		this.backlog = backlog;
	}
	
	public ArrayList<MulticastMessage> getBacklog(){
		return backlog;
	}
}
