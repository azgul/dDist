
/**
 *
 * @author Martin
 */
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import multicast.MulticastQueue;
import org.junit.*;
import static org.junit.Assert.*;
import replicated_calculator.Callback;
import replicated_calculator.ClientNonRobust;
import replicated_calculator.ClientTUI;
import week6.ServerReplicated;
import week6.multicast.ProperConnectorClient;

public class CalculatorTest {
	private int servers = 3;
	private int clients = 4;
	private int port = 1337;
	private ServerReplicated server[];
	private ProperConnectorClient client[];
	private int passesPerClient = 4;
	
	public void setup() {
		server = new ServerReplicated[servers];
		client = new ProperConnectorClient[clients*servers];
		
		for(int i=0; i<servers; i++)
			server[i] = new ServerReplicated();
		
		for(int i=0; i<(clients*servers); i++)
			client[i] = new ProperConnectorClient();
		
		int p = port;
		server[0].createGroup(port,p+1);	
		p++;
		
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
					client[c].connect(new InetSocketAddress(InetAddress.getLocalHost(), p+1), p+100+c, Integer.toString(c));
					c++;
				}
				p++;
				p++;
			}
		} catch (IOException e) {}
	}
	
	synchronized public static void report(String str) {
	System.out.println(str);
    }
	
	public void putNumbers(String[] vars) {
		Random r = new Random();
		int c, var1, var2, var3, type;
		BigInteger value;
		
		for(int i = 0; i < client.length*passesPerClient; i++){
			var1 = r.nextInt(vars.length);
			var2 = r.nextInt(vars.length);
			var3 = r.nextInt(vars.length);
			type = r.nextInt(2);
			c = r.nextInt(client.length);
			//System.out.println("Type: " + type);
			switch(type){
				// Add
				case 1:
					//System.out.println("Adding: " + vars[var3] + " = " + vars[var1] + " + " + vars[var2]);
					client[c].add(vars[var1], vars[var2], vars[var3]);
					
					break;
					
				
				// Assign
				default:
					value = new BigInteger(8, r);
					//System.out.println("Assigning: " + vars[var1] + " = " + value);
					client[c].assign(vars[var1], value);
					break;
			}
		}
	}
	
	/*@Test
	public void doesItWork() {
		setup();
		wait(5);
		
		String[] vars = {"a","b","c","b","e","f"};
		
		putNumbers(vars);
		
		wait(1);
		
		for(String s : vars){
			curr = null;
			prev = null;
			for(int i = 0; i < client.length; i++){
				final String cl = "Client" + i + ": " + s + " = ";
				client[i].read(s, new Callback<BigInteger>(){
					public void result(BigInteger bi){
						set(bi);
					}
				});
				wait(1);
				System.out.println(cl + curr);
				
				if (prev!=null)
					assertEquals(curr, prev);
				
				prev=curr;
			}
			wait(1);
		}
	}*/
	
	@Test
	public void doesNewServerGetOldVariables() {
		ProperConnectorClient c1 = new ProperConnectorClient();
		ProperConnectorClient c2 = new ProperConnectorClient();
		ServerReplicated s1 = new ServerReplicated();
		ServerReplicated s2 = new ServerReplicated();
		
		int serverP = 40000;
		int serverCP = 41000;
		int clientP = 42000;
		
		// starting server 1 up
		s1.createGroup(serverP, serverCP);
		wait(1);
		try {
			Random r = new Random();
			
			BigInteger bi = new BigInteger(8, r);
			// connecting to server 1
			c1.connect(new InetSocketAddress(InetAddress.getLocalHost(), serverCP), clientP, "1");
			c1.assign("a", bi);
			c1.assign("b", bi);
			c1.add("a", "b", "c");
			
			wait(5);

			// joining server 1 with server 2
			s2.joinGroup(serverP+1, new InetSocketAddress(InetAddress.getLocalHost(), serverP), serverCP+1);
			
			wait(5);

			c2.connect(new InetSocketAddress(InetAddress.getLocalHost(), serverCP+1), clientP+2, "2");
			//c2.disconnect();
			
			wait(5);

			c2.read("a", new Callback<BigInteger>(){
				public void result(BigInteger bi){
					System.out.println("Callback lolz");
				set(bi);
			}});

			wait(2);

			assertEquals("Should be equal", bi, curr);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		s1.leaveGroup();
		//s2.leaveGroup();
	}
	
	public void set(BigInteger bi) {
		curr = bi;
		System.out.println("It was set to " + bi);
	}
	
	private BigInteger curr, prev;
	
	public static void wait(int secs) {
		try {
			Thread.currentThread().sleep(secs*1000);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
