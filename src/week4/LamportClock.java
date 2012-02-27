package week4;

import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Martin
 */
public class LamportClock {
	
	private int clock;
	
	public LamportClock(){
		clock = 0;
	}
	
	public int getClock() {
		synchronized(this) {
			return clock;
		}
	}
	
	public int tick() {
		synchronized(this) {
			clock++;
			return clock;
		}
	}
	
	public void tick(AbstractLamportMessage msg) {
		synchronized(this) {		
		clock = Math.max(msg.getClock(), clock)+1;
		}
	}
	
}
