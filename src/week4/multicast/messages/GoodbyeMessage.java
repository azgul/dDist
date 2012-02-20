/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

import java.net.InetSocketAddress;
import multicast.MulticastMessage;

/**
 *
 * @author Lars Rasmussen
 */
public class GoodbyeMessage extends AbstractLamportMessage{
	public GoodbyeMessage(InetSocketAddress addr){
		super(addr);
	}
}
