
/**
 *
 * @author Martin
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import multicast.MulticastQueue;
import org.junit.*;
import static org.junit.Assert.*;
import replicated_calculator.ClientNonRobust;
import week6.multicast.CalculatorQueue;
import week6.multicast.ProperConnectorClient;

public class CalculatorTest {
	private int servers = 3;
	private int clients = 4;
	private int port = 1337;
	private CalculatorQueue server[];
	private ProperConnectorClient client[];
	
	@Before
	public void setup() {
		server = new CalculatorQueue[servers];
		client = new ProperConnectorClient[clients*servers];
		
		for(int i=0; i<servers; i++)
			server[i] = new CalculatorQueue();
		
		for(int i=0; i<(clients*servers); i++)
			client[i] = new ProperConnectorClient();
		
		int p = port;
		try {
			server[0].createGroup(port, MulticastQueue.DeliveryGuarantee.NONE);	
		} catch (IOException e) {}
		
		try {
			for (int i=1; i<servers; i++) {
				p++;
				server[i].joinGroup(p, new InetSocketAddress(InetAddress.getLocalHost(), port), MulticastQueue.DeliveryGuarantee.TOTAL);
				wait(1);
			}

			p = port;
			int c = 0;
			for (int i=0; i<servers; i++) {
				for (int j=0; i<clients; j++) {
					client[c].connect(new InetSocketAddress(InetAddress.getLocalHost(), p), p+100, Integer.toString(c));
					c++;
				}
				p++;
			}
		} catch (IOException e) {}
	}
	
	@Test
	public void doesItWork() {
		
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
