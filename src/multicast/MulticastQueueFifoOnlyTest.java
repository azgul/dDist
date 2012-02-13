package multicast;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.io.IOException;

/**
 * This is a test run of MulticastQueueFifoOnly which checks that each
 * peer receives each message exactly once.
 *
 * @author Jesper Buus Nielsen, Aarhus University, 2012.
 */
class MulticastQueueFifoOnlyTest {

    public static void wait(int secs) {
	try {
	    Thread.currentThread().sleep(secs*1000);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(-1);
	}
    }

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
	        final int testSize = 15;

		InetAddress localhost = InetAddress.getLocalHost();

		MulticastQueueFifoOnly<Integer>[] peer 
		    = new MulticastQueueFifoOnly[3];

		for (int p=0; p<3; p++) {
		    peer[p] = new MulticastQueueFifoOnly<Integer>(); 
		}

		System.out.println("FORMING GROUP");

		// Let peer0 create a group.
		peer[0].createGroup(44440, 
				    MulticastQueue.DeliveryGuarantee.FIFO);
		//		peer[0].printLog();
		wait(1);

		// Let peer1 join at peer0 
		peer[1].joinGroup(44441, new InetSocketAddress(localhost,44440),
				  MulticastQueue.DeliveryGuarantee.FIFO);
		//		peer[1].printLog();
		wait(1);

		// Let peer2 join at peer1 
		peer[2].joinGroup(44442, new InetSocketAddress(localhost,44441),
				  MulticastQueue.DeliveryGuarantee.FIFO);
		//		peer[2].printLog();
		wait(1);

		System.out.println("BROADCASTING");

		// Let them all send at the same time. 
		for (int i=0; i<testSize; i++) {
		    peer[i % 3].put(Integer.valueOf(i));
		}

		wait(10);

		System.out.println("LEAVING GROUP");

		peer[2].leaveGroup();
		wait(1);

		peer[0].leaveGroup();		
		wait(1);

		peer[1].leaveGroup();
		wait(1);

		/* We now see what each peer received. We print it,
		 * and we also tabulate it, to check that they all
		 * received each message exactly once.
		 */
		for (int p = 0; p<3; p++) {
		    System.out.println("RECEIVED BY peer["+p+"]");
		    boolean[] received = new boolean[testSize];
		    MulticastMessage msg;
		    while ((msg = peer[p].get()) != null) {
			System.out.println("       " + msg);
			if (msg instanceof MulticastMessagePayload) {
			    int theInt = ((MulticastMessagePayload<Integer>)msg).getPayload().intValue();
			    if (received[theInt]==true) {
				System.out.println("Test result: FAIL!\n   Reason: Peer " + p + " received " + theInt + " twice");
				System.exit(-1);
			    } 
			    received[theInt]=true;
			}
		    }
		    for (int i=0; i<testSize; i++) {
			if (received[i]==false) {
			    System.out.println("Test result: FAIL!\n   Reason: Peer " + p + " did not receive " + i);
			    System.exit(-1);
			} 			
		    }
		}

		System.out.println("\nTest result: OK!");
	}
}
