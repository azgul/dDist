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
public class BacklogMessage extends LamportMulticastMessage {
	private ArrayList<LamportMulticastMessage> backlog;
	
	public BacklogMessage(InetSocketAddress addr, ArrayList<LamportMulticastMessage> backlog, int clock){
		super(addr, clock);
		this.backlog = backlog;
	}
	
	public ArrayList<LamportMulticastMessage> getBacklog(){
		return backlog;
	}
}
