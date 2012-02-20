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
public class WelcomeMessage extends AbstractLamportMessage{
	public WelcomeMessage(InetSocketAddress addr){
		super(addr);
	}
}
