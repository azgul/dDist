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
	public double getClock();
	public void setClock(double clock);
}
