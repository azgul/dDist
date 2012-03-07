package multicastqueue;

import java.net.InetSocketAddress;
import java.io.Serializable;

/**
 * The root of the hierarchy of messages which are returned by MulticastQueue.
 *
 * @author Jesper Buus Nielsen, Aarhus University, 2012.
 */
public class MulticastMessage implements Serializable
{
	private InetSocketAddress sender;
	private Timestamp timestamp;

	public MulticastMessage(InetSocketAddress sender, Timestamp timestamp)
	{
		this.sender = sender;
		this.timestamp = timestamp;
	}

	/**
	 * @return The sender of the message.
	 */
	public InetSocketAddress getSender()
	{
		return sender;
	}

	public Timestamp getTimestamp()
	{
		return timestamp;
	}

	public String toString()
	{
		return "(MulticastMessage from " + sender + ")";
	}
}
