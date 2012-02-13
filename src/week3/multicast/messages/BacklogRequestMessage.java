/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week3.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class BacklogRequestMessage extends MulticastMessage {
	public BacklogRequestMessage(InetSocketAddress addr){
		super(addr);
	}
}
