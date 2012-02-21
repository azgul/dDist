import java.io.IOException;
import java.net.InetSocketAddress;
import multicast.MulticastMessage;
import multicast.MulticastQueue;
import org.junit.*;
import static org.junit.Assert.*;
import week4.*;
import week4.multicast.*;

/**
 *
 * @author Martin
 */
public class TotallyOrderedMultiCastStressTest {
	private int port = 1337;
	private int peers = 5;
	private int passes = 5;
	private ChatQueue[] queue;
	
	@Before
	public void setup() {
		queue = new ChatQueue[peers];
		
		for(int p=0; p<peers; p++)
			queue[p] = new ChatQueue();
		
		try {
			queue[0].createGroup(port, MulticastQueue.DeliveryGuarantee.FIFO);
			queue[1].joinGroup(port+1, new InetSocketAddress("localhost", port), MulticastQueue.DeliveryGuarantee.FIFO);
			queue[2].joinGroup(port+2, new InetSocketAddress("localhost", port+1), MulticastQueue.DeliveryGuarantee.FIFO);
			queue[3].joinGroup(port+3, new InetSocketAddress("localhost", port+2), MulticastQueue.DeliveryGuarantee.FIFO);
			queue[4].joinGroup(port+4, new InetSocketAddress("localhost", port+3), MulticastQueue.DeliveryGuarantee.FIFO);
			
		} catch (IOException e) {}
	}
	
	@Test
	public void doesItWork() {
		for (int i=0; i<passes;i++) {
			for (ChatQueue q : queue)
				q.put(Integer.toString(i));
		}
		
		MulticastMessage[] message = new MulticastMessage[peers];
		
		for (int i=0; i<(passes*peers);i++) {
			for (int j=0; j<peers; j++) {
				message[j] = queue[j].get();
			}
							
			for (int k=0; k<peers; k++) {
				if (k+1 < message.length)
					assertEquals(message[k].toString(), message[k+1].toString());
			}
		}
	}
}
