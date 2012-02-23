import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicast.MulticastQueue;
import org.junit.*;
import static org.junit.Assert.*;
import week4.multicast.*;
import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Martin
 */
public class TotallyOrderedMultiCastStressTest{
	private int port = 1337;
	private int peers = 3;
	private int passes = 2;
	private ChatQueue[] queue;
	
	@Before
	public void setup() {
		queue = new ChatQueue[peers];
		
		for(int p=0; p<peers; p++)
			queue[p] = new ChatQueue();
		
		try {
			queue[0].createGroup(port, MulticastQueue.DeliveryGuarantee.TOTAL);
			for (int i=1; i<peers; i++) {
				queue[i].joinGroup(port+1, new InetSocketAddress("localhost", port), MulticastQueue.DeliveryGuarantee.TOTAL);
				port++;
			}
			
		} catch (IOException e) {}
		System.out.println("Created 1 server and connected with "+ (peers-1) + " peers");
	}
	
	@Test
	public void doesItWork() {
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			System.err.println("Interrupted...");
			return;
		}
		
		int x = 0;
		for (int i=0; i<passes;i++) {
			for (int j=0; j<peers; j++) {
				queue[j].put(Integer.toString(x)); 
				x++;
			}
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		System.out.println(x + " messages was sent");
		
		AbstractLamportMessage[] message;
		
		for (int i=0; i<(passes*peers);i++) {
			message = null;
			message = new AbstractLamportMessage[peers];
			for (int j=0; j<peers; j++) {
				message[j] = queue[j].get();
					while (!queue[j].shouldHandleMessage(message[j]))
						message[j] = queue[j].get();
			}
							
			for (int k=0; k<peers; k++) {
				System.out.println("Peer "+(k+1)+" received: "+message[k]);
				if (k+1 < message.length) {
					assertEquals(message[k], message[k+1]);}
			}
		}
		
		for (int i=0; i<peers; i++) 
			queue[i].leaveGroup();
		
		for (int i=0; i<peers; i++)
			try {
				queue[i].join();
			} catch (InterruptedException e) {}
	}
}
