import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.PriorityQueue;
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
	private int passes = 100;
	private ChatQueue[] queue;
	
	@Before
	public void setup() {
		queue = new ChatQueue[peers];
		
		for(int p=0; p<peers; p++)
			queue[p] = new ChatQueue();
		
		try {
			queue[0].createGroup(port, MulticastQueue.DeliveryGuarantee.TOTAL);
			
			for (int i=1; i<peers; i++) {
				queue[i].joinGroup(port+1, new InetSocketAddress(InetAddress.getLocalHost(), port), MulticastQueue.DeliveryGuarantee.TOTAL);
				port++;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
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
			for (int j=peers-1; j>=0; j--) {
				queue[j].put(Integer.toString(x)); 
				x++;
			}
		}
		System.out.println(x + " messages were sent");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		/*System.out.println("Stopped sleeping");
		for (int i=0;i<peers;i++) {
			//System.out.println("queue: " + i);
			PriorityQueue<AbstractLamportMessage> pendingGets = new PriorityQueue<AbstractLamportMessage>(queue[i].pendingGets);
		
			AbstractLamportMessage msg = pendingGets.poll();
			while (msg!=null) {
				//System.out.println(msg);
				msg = pendingGets.poll();
			}
		}*/
		
		
		
		for (int i=0; i<(passes*peers);i++) {			
			AbstractLamportMessage curr = null;
			AbstractLamportMessage prev = null;
			
			for (int j=0; j<peers; j++) {
				curr = queue[j].get();
				
				while (!queue[j].shouldHandleMessage(curr))
					curr = queue[j].get();
				
				
				if (prev!=null) {
					//System.out.println("#"+j+" Comparing '"+prev+"' to '"+curr+"': "+prev.toString().equals(curr.toString()));
					System.out.println("#"+j+" Comparing '"+prev+"'("+prev.getClock()+") to '"+curr+"' ("+curr.getClock()+"): "+prev.toString().equals(curr.toString()));
					assertEquals(curr.toString(), prev.toString());
					
					//System.out.println("Peer "+(j+1)+" received: "+curr);
				}
				
				prev=curr;
				
			}
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		/*for (int i=0;i<peers;i++) {
			HashSet<InetSocketAddress> hasConnectionToUs = new HashSet<InetSocketAddress> (queue[i].hasConnectionToUs);
			System.out.println("Connections: " + queue[i].hasConnectionToUs.size());
			
			
			for (InetSocketAddress inet : hasConnectionToUs)
				System.out.println(queue[i].myAddress.getPort() + " is connected to from " + inet.getPort());
		}*/
		
		
		for (int i=0; i<peers; i++) 
			queue[i].leaveGroup();
		
		try {
			for (int i=0; i<peers; i++)
				queue[i].join();
		} catch (InterruptedException e) {}
	}
}
