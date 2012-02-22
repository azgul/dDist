/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week4;

import javax.swing.JTextArea;
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
	private JTextArea chat;
	
	public ChatListener(MulticastQueue queue, JTextArea chatArea){
		this.queue = queue;
		chat = chatArea;
	}

	@Override
	public void run() {
		MulticastMessage msg;
		
		try{
			while(true){
				if((msg = queue.get()) != null){
					//System.out.println(msg.toString());
					chat.append(msg.toString() + "\r\n");
					chat.setCaretPosition(chat.getDocument().getLength());
				}
				
				Thread.sleep(timeout);
			}
		}catch(InterruptedException e){
			// Stop
			System.out.println("Interrupted D:");
		}
	}
	
}
