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
	private int peers = 5;
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
				wait(1);
			}
			
		} catch (IOException e) {}
		System.out.println("Created 1 server and connected with "+ (peers-1) + " peers");
	}
	
	@Test
	public void doesItWork() {		
		int x = 0;
		for (int i=0; i<passes;i++) {
			for (int j=0; j<peers; j++) {
				queue[j].put(Integer.toString(x)); 
				x++;
			}
		}
		System.out.println(x + " messages were sent");
		
		wait(peers*3);		
		
		for (int i=0; i<(passes*peers);i++) {			
			AbstractLamportMessage curr = null;
			AbstractLamportMessage prev = null;
			
			for (int j=0; j<peers; j++) {
				curr = queue[j].get();
				
				while (!queue[j].shouldHandleMessage(curr))
					curr = queue[j].get();
				
				if (prev!=null) {
					//System.out.println("#"+j+" Comparing '"+prev+"'("+prev.getClock()+") to '"+curr+"' ("+curr.getClock()+"): "+prev.toString().equals(curr.toString()));
					assertEquals(curr.toString(), prev.toString());
				}
				
				prev=curr;
			}
		}
		
		wait(1);
		/*
		for (int i=0; i<peers; i++) {
			queue[i].leaveGroup();
			wait(1);
		}*/
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
