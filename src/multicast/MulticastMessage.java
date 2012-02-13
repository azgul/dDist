package multicast;

import java.net.InetSocketAddress;
import java.io.Serializable;

/**
 * The root of the hierarchy of messages which are returned by MulticastQueue.
 *
 * @author Jesper Buus Nielsen, Aarhus University, 2012.
 */
class MulticastMessage implements Serializable {
    private InetSocketAddress sender;
    /**
     * @param sender The sender of the message.
     */
    MulticastMessage(InetSocketAddress sender) {
	this.sender = sender;
    }
    /**
     * @return The sender of the message.
     */
    public InetSocketAddress getSender() {
	return sender;
    }
    public String toString() {
	return "(MulticastMessage from " + sender + ")";
    }
}
