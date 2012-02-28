/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

import multicast.MulticastMessage;
import multicast.MulticastQueue;
import replicated_calculator.ClientEventVisitor;
import week6.multicast.CalculatorQueue;
import week6.multicast.messages.ClientEventMessage;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ServerListener extends Thread {
	CalculatorQueue queue;
	ClientEventVisitor visitor;
	long timeout = 1;
	
	public ServerListener(CalculatorQueue queue, ClientEventVisitor v){
		this.queue = queue;
		visitor = v;
	}

	@Override
	public void run() {
		ClientEventMessage msg;
		
		try{
			while(true){
				if((msg = queue.get()) != null){
						msg.getClientEvent().accept(visitor);
				}
				
				Thread.sleep(timeout);
			}
		}catch(InterruptedException e){
			// Stop
			System.out.println("Interrupted D:");
		}
	}
}
