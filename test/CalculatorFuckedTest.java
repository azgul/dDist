
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import replicated_calculator.Callback;
import replicated_calculator.ClientNonRobust;
import week6.ServerReplicated;
import week6.multicast.ProperConnectorClient;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author larss
 */
public class CalculatorFuckedTest {
	private final int serverPort = 1337;
	private final int clientPort = 1338;
	
	public CalculatorFuckedTest(){
		clientCrashTest();
	}
	
	private void clientCrashTest(){
		ServerReplicated s1 = new ServerReplicated();
		s1.createGroup(serverPort, clientPort);
		
		wait(1);
		

		ServerReplicated s2 = new ServerReplicated();
		try{
			s2.joinGroup(serverPort+123, new InetSocketAddress(InetAddress.getLocalHost(), serverPort), clientPort+100);
		}catch(UnknownHostException e){
			System.err.println("Fucked host, y1");
			return;
		}
		
		ProperConnectorClient c = new ProperConnectorClient();
		try{
			c.connect(new InetSocketAddress(InetAddress.getLocalHost(), clientPort), clientPort+1, "IWILLFUCKYOUUP");
		}catch(UnknownHostException e){
			System.err.println("Fucked host, y0");
			return;
		}

		c.disconnect();
		
		wait(1);
		
		s1.leaveGroup();
		s2.leaveGroup();
		
		System.exit(0);
	}
	
	public static void main(String[] args){
		new CalculatorFuckedTest();
	}
	
	private void wait(int secs) {
		try {
			Thread.currentThread().sleep(secs*1000);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
