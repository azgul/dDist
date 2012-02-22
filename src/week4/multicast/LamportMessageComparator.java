/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast;

import java.util.Comparator;
import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class LamportMessageComparator implements Comparator<AbstractLamportMessage> {

	public int compare(AbstractLamportMessage t, AbstractLamportMessage t1) {
		return t.getClock() - t1.getClock();
	}	
}
