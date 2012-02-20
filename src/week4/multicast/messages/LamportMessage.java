/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4.multicast.messages;

/**
 *
 * @author larss
 */
public interface LamportMessage {
	public int getClock();
	public void setClock(int clock);
}
