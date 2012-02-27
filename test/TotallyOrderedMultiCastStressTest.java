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
	private int peers = 21;
	private int passes = 100;
	private ChatQueue[] queue;
	long before, temp, after;
	
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
				wait(1);
			}
			
		} catch (IOException e) {}
		System.out.println("Created 1 server and connected with "+ (peers-1) + " peers");
	}
	
	@Test
	public void doesItWork() {	
		before = System.currentTimeMillis();
		for (int i=0; i<passes;i++) {
			queue[i % peers].put(Integer.toString(i));
			/*for (int j=0; j<peers; j++) {
				queue[j].put(Integer.toString(x)); 
				x++;
			}*/
		}
		after = System.currentTimeMillis();
		temp = after-before;
		
		System.out.println(passes + " messages were sent");
		
		wait(peers);		
		
		before = System.currentTimeMillis();
		
		for (int i=0; i<passes;i++) {			
			AbstractLamportMessage curr;
			AbstractLamportMessage prev = null;
			
			for (int j=0; j<peers; j++) {
				curr = queue[j].get();
				
				while (!queue[j].shouldHandleMessage(curr))
					curr = queue[j].get();
				
				if (prev!=null) {
					assertEquals("curr: "+curr.getClock()+" -- prev: "+prev.getClock(),curr.toString(), prev.toString());
				}
				
				prev=curr;
			}
		}
		after = System.currentTimeMillis();
		temp += after-before;
		
		System.out.println(String.format("Benchmark took %s ms", temp));
		
		for (int i=0; i<peers; i++) {
			queue[i].leaveGroup();
			try {
				queue[i].join();
			} catch (InterruptedException e) {}
			wait(1);
		}
		System.out.println("Left group.");
	}
	
	public static void wait(int secs) {
		try {
			Thread.currentThread().sleep(secs*1000);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}