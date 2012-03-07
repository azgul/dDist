package multicastqueue;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jacob
 * Date: 26-02-12
 * Time: 21:52
 */
public class Frame implements Comparable<Frame>
{
	private MulticastMessage message;
	private Set<InetSocketAddress> missingPeers;
	private Timestamp time;

	public Frame(MulticastMessage message, Set<InetSocketAddress> missingPeers)
	{
		this.message = message;
		this.missingPeers = missingPeers;
		this.time = message.getTimestamp();
	}

	public Frame(Set<InetSocketAddress> missingPeers, Timestamp time)
	{
		this.missingPeers = missingPeers;
		this.time = time;
		this.message = null;
	}

	public int compareTo(Frame o)
	{
		return time.compareTo(o.time);
	}

	public Timestamp getTimestamp()
	{
		return time;
	}

	public void removePeer(InetSocketAddress peer)
	{
		missingPeers.remove(peer);
	}

	public boolean missingPeerIsEmpty()
	{
		return missingPeers.isEmpty();
	}

	public MulticastMessage getMessage()
	{
		return message;
	}

	public Set<InetSocketAddress> getMissingPeers()
	{
		return missingPeers;
	}
}
