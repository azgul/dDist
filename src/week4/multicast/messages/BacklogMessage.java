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
public class BacklogMessage extends AbstractLamportMessage {
	private ArrayList<AbstractLamportMessage> backlog;
	
	public BacklogMessage(InetSocketAddress addr, ArrayList<AbstractLamportMessage> backlog){
		super(addr);
		this.backlog = backlog;
	}
	
	public ArrayList<AbstractLamportMessage> getBacklog(){
		return backlog;
	}
}
