/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6.multicast.messages;

import week4.multicast.messages.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class VariablesMessage extends AbstractLamportMessage {
	private ArrayList<ClientEventMessage> backlog;
	
	public VariablesMessage(InetSocketAddress addr, ArrayList<ClientEventMessage> backlog){
		super(addr);
		this.backlog = backlog;
	}
	
	public ArrayList<ClientEventMessage> getBacklog(){
		return backlog;
	}
	
	public String toString(){
		return "[Here's a backlog for you~]";
	}
}
