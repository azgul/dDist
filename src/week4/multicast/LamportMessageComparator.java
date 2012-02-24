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
		if(t.getClock() > t1.getClock())
			return 1;
		else if(t.getClock() == t1.getClock())
			return 0;
		else
			return -1;
		
		/*double scaling = Math.pow(10, 11);
		
		double clock1 = t.getClock() * scaling;
		double clock2 = t1.getClock() * scaling;
		
		double comp = clock1 - clock2;
		//double comp = Math.floor(t.getClock() - t1.getClock());
		//int comp = (int) (Math.floor(t.getClock()) - Math.floor(t1.getClock()));
		return (int) comp;
		
		//return (int) (t.getClock() - t1.getClock());*/
	}	
}
