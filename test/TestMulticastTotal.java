import multicast.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import week4.multicast.ChatQueue;

/**
 * Created by IntelliJ IDEA.
 * User: Jacob
 * Date: 20-02-12
 * Time: 20:39
 */
public class TestMulticastTotal
{

	public static void wait(int secs)
	{
		try
		{
			Thread.currentThread().sleep(secs * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		final int testSize = 100;

		InetAddress localhost = InetAddress.getLocalHost();

		ChatQueue[] peer
				= new ChatQueue[4];

		for (int p = 0; p < 4; p++)
		{
			peer[p] = new ChatQueue();
		}

		System.out.println("FORMING GROUP");

		// Let peer0 create a group.
		peer[0].createGroup(44440,
				MulticastQueue.DeliveryGuarantee.TOTAL);
				//peer[0].printLog();
		wait(1);

		// Let peer1 join at peer0 
		peer[1].joinGroup(44441, new InetSocketAddress(localhost, 44440),
				MulticastQueue.DeliveryGuarantee.TOTAL);
				//peer[1].printLog();
		wait(1);
        
		// Let peer2 join at peer1 
		peer[2].joinGroup(44442, new InetSocketAddress(localhost, 44441),
				MulticastQueue.DeliveryGuarantee.TOTAL);
				//peer[2].printLog();
		wait(1);
		System.out.println("BROADCASTING");

		// Let them all send at the same time. 
		for (int i = 0; i < testSize; i++)
		{
			peer[i % 3].put(String.valueOf(i));

		}

		wait(1);

		System.out.println("RECEIVING");
		/* We now see what each peer received. We print it,
		 * and we also tabulate it, to check that they all
		 * received each message exactly once.
		 */
		for (int p = 0; p < testSize * 3; p++)
		{
			System.out.print(p%3 +":" + peer[p%3].get());

			if(p % 3 == 2)
				System.out.println();
			else
				System.out.print("\t|");
		}



		peer[3].joinGroup(44443, new InetSocketAddress(localhost, 44441),
				MulticastQueue.DeliveryGuarantee.TOTAL);
		//		peer[2].printLog();
		wait(5);

		System.out.println("BROADCASTING");

		// Let them all send at the same time.
		for (int i = 0; i < testSize; i++)
		{
			peer[i % 4].put(String.valueOf(i));
		}

		wait(1);

		System.out.println("RECEIVING");
		/* We now see what each peer received. We print it,
		 * and we also tabulate it, to check that they all
		 * received each message exactly once.
		 */
		for (int p = 0; p < testSize * 4; p++)
		{
			System.out.print(p%4 +":" + peer[p%4].get());

			if(p % 4 == 3)
				System.out.println();
			else
				System.out.print("\t|");
		}
		peer[0].leaveGroup();
		peer[1].leaveGroup();
		peer[2].leaveGroup();
		peer[3].leaveGroup();
	}
}