package week3.multicast;


import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import multicast.*;
import week3.MultiChat.Messages.MulticastChatMessage;


public class ChatQueue extends Thread implements MulticastQueue<Serializable>{
	/**
     * The address on which we listen for incoming messages.
     */      
    private InetSocketAddress myAddress;

    /**
     * Used to signal that the queue is leaving the peer group. 
     */
    private boolean isLeaving = false;
	
    /**
     * The thread which handles outgoing traffic.
     */
    private SendingThread sendingThread;

    /**
     * The peers who have a connection to us. Used to make sure that
     * we do not close down the receiving end of the queue before all
     * sending to the queue is done. Not strictly needed, but nicer.
     */
    private HashSet<InetSocketAddress> hasConnectionToUs;
	
	/**
     * The incoming message queue. All other peers send their messages
     * to this queue.
     */
    private PointToPointQueueReceiverEnd<MulticastMessage> incoming;

    /**
     * Keeping track of the outgoing message queues, stored under the
     * corresponding internet address.
     */
    private ConcurrentHashMap<InetSocketAddress,PointToPointQueueSenderEnd<MulticastMessage>> outgoing;

    /**
     * Objects pending delivering locally.
     */
    private ConcurrentLinkedQueue<MulticastMessage> pendingGets;
    
    /**
     * Objects pending sending.
     */
    private ConcurrentLinkedQueue<String> pendingSends;
	
	public ChatQueue(){
		incoming = new PointToPointQueueReceiverEndNonRobust<MulticastMessage>();
		pendingGets = new ConcurrentLinkedQueue<MulticastMessage>();
		pendingSends = new ConcurrentLinkedQueue<String>();
		outgoing = new ConcurrentHashMap<InetSocketAddress,PointToPointQueueSenderEnd<MulticastMessage>>();
	}
	
	public void createGroup(int port, DeliveryGuarantee deliveryGuarantee) throws IOException {
	assert (deliveryGuarantee==DeliveryGuarantee.NONE || 
		deliveryGuarantee==DeliveryGuarantee.FIFO) 
	    : "Can at best implement FIFO";
	// Try to listen on the given port. Exception are propagated out.
	incoming.listenOnPort(port);

        // Record our address
	InetAddress localhost = InetAddress.getLocalHost();
	String localHostAddress = localhost.getCanonicalHostName();
	myAddress = new InetSocketAddress(localHostAddress, port);

	// Buffer a message that we have joined the group.
	addAndNotify(pendingGets, new MulticastMessageJoin(myAddress));

	// Start the receiveing thread.
	this.start();
    }

    public void joinGroup(int port, InetSocketAddress knownPeer, 
			  DeliveryGuarantee deliveryGuarantee) 
			    throws IOException {
        assert (deliveryGuarantee==DeliveryGuarantee.NONE || 
		deliveryGuarantee==DeliveryGuarantee.FIFO) 
	    : "Can at best implement FIFO";

	// Try to listen on the given port. Exceptions are propagated
	// out of the method.
	incoming.listenOnPort(port);

        // Record our address.
	InetAddress localhost = InetAddress.getLocalHost();
	String localHostAddress = localhost.getCanonicalHostName();
	myAddress = new InetSocketAddress(localHostAddress, port);

	// Make an outgoing connection to the known peer.
	PointToPointQueueSenderEnd<MulticastMessage> out 
	    = connectToPeerAt(knownPeer);	
	// Send the known peer our address. 
	MulticastQueueFifoOnly.JoinRequestMessage joinRequestMessage 
	    = new MulticastQueueFifoOnly.JoinRequestMessage(myAddress);
	out.put(joinRequestMessage);
	// When the known peer receives the join request it will
	// connect to us, so let us remember that she has a connection
	// to us.
	hasConnectionToUs.add(knownPeer);	

	// Buffer a message that we have joined the group.
	addAndNotify(pendingGets, new MulticastMessageJoin(myAddress));

	// Start the receiving thread
	this.start();
    }

	@Override
	public void put(Serializable object) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MulticastMessage get() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void leaveGroup() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	
	/**
     * Will take objects from pendingSends and send them to all peers.
     * If the queue empties and leaveGroup() was called, then the
     * queue will remain empty, so we can terminate.
     */
    public class SendingThread extends Thread {
		public void run() {	    
			log("starting sending thread.");
			// As long as we are not leaving or there are objects to
			// send, we will send them.
			waitForPendingSendsOrLeaving();
			Object msg;
			while ((msg = pendingSends.poll()) != null) {
				sendToAll(new MulticastChatMessage(myAddress, (String)msg));
				waitForPendingSendsOrLeaving();
			}
			log("shutting down outgoing connections.");
			synchronized (outgoing) {
			for (InetSocketAddress address : outgoing.keySet()) {
				disconnectFrom(address);
			}
			}
			log("stopping sending thread.");
		}
    }
	
	/**
     * Used by the sending thread to wait for objects to enter the
     * collection or us having left the group. When the method
     * returns, then either the collection is non-empty, or the
     * multicast queue was called in leaveGroup();
     */
    private void waitForPendingSendsOrLeaving() {
	synchronized (pendingSends) {
	    while (pendingSends.isEmpty() && !isLeaving) {
		try {
		    // We will be woken up if an object arrives or we
		    // are leaving the group. Both might be the case
		    // at the same time.
		    pendingSends.wait();
		} catch (InterruptedException e) {
		    // Probably leaving. The while condition will
		    // ensure proper behavior in case of some other
		    // interruption.
		}
	    }
	    // Now: pendingSends is non empty or we are leaving the group.
	}	
    }
}