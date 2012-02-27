
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
import week6.ServerReplicated;
import week6.multicast.CalculatorQueue;
import week6.multicast.ProperConnectorClient;

public class CalculatorTest {
	private int servers = 3;
	private int clients = 4;
	private int port = 1337;
	private ServerReplicated server[];
	private ProperConnectorClient client[];
	
	@Before
	public void setup() {
		server = new ServerReplicated[servers];
		client = new ProperConnectorClient[clients*servers];
		
		for(int i=0; i<servers; i++)
			server[i] = new ServerReplicated();
		
		for(int i=0; i<(clients*servers); i++)
			client[i] = new ProperConnectorClient();
		
		int p = port;
		server[0].createGroup(port);	
		
		try {
			for (int i=1; i<servers; i++) {
				p++;
				server[i].joinGroup(p, new InetSocketAddress(InetAddress.getLocalHost(), port), p+1);
				p++;
				wait(1);
			}

			p = port;
			int c = 0;
			for (int i=0; i<servers; i++) {
				for (int j=0; j<clients; j++) {
					System.out.println("Connecting with: " + c);
					client[c].connect(new InetSocketAddress(InetAddress.getLocalHost(), p+1), p+100, Integer.toString(c));
					c++;
				}
				p++;
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
