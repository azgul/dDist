/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week3;

import multicast.MulticastMessage;
import multicast.MulticastMessageLeave;
import multicast.MulticastQueue;

/**
 *
 * @author larss
 */
public class ChatListener extends Thread{
	MulticastQueue queue;
	long timeout = 1;
	
	public ChatListener(MulticastQueue queue){
		this.queue = queue;
	}

	@Override
	public void run() {
		MulticastMessage msg;
		
		try{
			while(true){
				if((msg = queue.get()) != null)
					System.out.println(msg.toString());
				
				Thread.sleep(timeout);
			}
		}catch(InterruptedException e){
			// Stop
			System.out.println("Interrupted D:");
		}
	}
	
}
