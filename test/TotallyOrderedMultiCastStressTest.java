import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicast.MulticastMessage;
import multicast.MulticastQueue;
import org.junit.*;
import static org.junit.Assert.*;
import week4.*;
import week4.multicast.*;
import week4.multicast.messages.AbstractLamportMessage;

/**
 *
 * @author Martin
 */
public class TotallyOrderedMultiCastStressTest {
	private int port = 1337;
	private int peers = 2;
	private int passes = 1;
	private ChatQueue[] queue;
	
	@Before
	public void setup() {
		queue = new ChatQueue[peers];
		
		for(int p=0; p<peers; p++)
			queue[p] = new ChatQueue();
		
		try {
			queue[0].createGroup(port, MulticastQueue.DeliveryGuarantee.FIFO);
			for (int i=1; i<peers; i++) {
				queue[i].joinGroup(port+1, new InetSocketAddress("localhost", port), MulticastQueue.DeliveryGuarantee.FIFO);
				port++;
			}
			
		} catch (IOException e) {}
	}
	
	@Test
	public void doesItWork() {
		wait(3);
		
		for (int i=0; i<passes;i++) {
			for (int j=0; j<peers; j++)
				queue[j].put(Integer.toString(j));
		}
		
		wait(1);
		
		for(int i = 0; i < peers; i++){
			queue[i].leaveGroup();
			wait(1);
		}
		
		HashMap<Integer,ArrayList<AbstractLamportMessage>> messages = new HashMap<Integer,ArrayList<AbstractLamportMessage>>();
		AbstractLamportMessage msg;
		
		for(int i = 0; i < peers; i++){
			while((msg = queue[i].get()) != null){
				if(!messages.containsKey(i))
					messages.put(i, new ArrayList<AbstractLamportMessage>());
				
				if(queue[i].shouldHandleMessage(msg)){
					ArrayList<AbstractLamportMessage> message = messages.get(i);
					message.add(msg);
				}
				System.out.println(":Æ");
			}
			System.out.println("Hvad skal jeg skrive?");
		}
		System.out.println("WHATUP FAGS");
		System.out.println(messages);
		
		/*for (int i=0; i<(passes*peers);i++) {
			for (int j=0; j<peers; j++) {
				message[j] = queue[j].get();
					while (!queue[j].shouldHandleMessage(message[j]))
						message[j] = queue[j].get();
			}
							
			for (int k=0; k<peers; k++) {
				if (k+1 < message.length)
					assertEquals(message[k].toString(), message[k+1].toString());
			}
			
			message = new AbstractLamportMessage[peers];
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
