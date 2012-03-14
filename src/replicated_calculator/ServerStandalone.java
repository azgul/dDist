package replicated_calculator;
import point_to_point_queue.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.net.InetSocketAddress;
import java.util.Queue;
import week6.ClientEventConnectDenied;

/**
 * 
 * An stand-along implementation of a server. It simply keeps the variables in 
 * a hash map and executes the commands in the natural way on this hash map.
 * 
 * @author Jesper Buus Nielsen, Aarhus University, 2012.
 *
 */

public class ServerStandalone extends Thread implements ClientEventVisitor, Server {

    protected final HashMap<String,BigInteger> valuation 
	= new HashMap<String,BigInteger>();
    protected final HashMap<String,PointToPointQueueSenderEndNonRobust<ClientEvent>> clients 
	= new HashMap<String,PointToPointQueueSenderEndNonRobust<ClientEvent>>();
    
    public void createGroup(int port) {
	operationsFromClients = null;
	try {
	    operationsFromClients = new PointToPointQueueReceiverEndNonRobust<ClientEvent>();
	    operationsFromClients.listenOnPort(Parameters.serverPortForClients);
	} catch (IOException e) {
	    System.err.println("Cannot start server!");
	    System.err.println("Check your network connection!");
	    System.err.println("Check that no other service runs on port " + Parameters.serverPortForClients + "!");
	    System.err.println();
	    System.err.println(e);
	    System.exit(-1);
	}		
	this.start();
    }
    
    public void joinGroup(InetSocketAddress knownPeer) {
	throw new UnsupportedOperationException("joinGroup");
    }
    
    /**
     * No group to leave, so simply shutsdown.
     */
    public void leaveGroup() {
	operationsFromClients.shutdown();
	for (String client : clients.keySet()) {
	    clients.remove(client).shutdown();
	}
	
    }
    
    protected BigInteger valuate(String var) {
		BigInteger val;
		synchronized(valuation) {
			val = valuation.get(var);
		}
		if (val==null) {
			val = BigInteger.ZERO;
		}
		return val;
    }
    
    protected void acknowledgeEvent(ClientEvent event) {
		synchronized(clients) {
			PointToPointQueueSenderEndNonRobust<ClientEvent> toClient = clients.get(event.clientName);
			toClient.put(event);
		}
    }
    
    public void visit(ClientEventAdd eventAdd) {
		synchronized(valuation) {
			System.out.println("Adding "+eventAdd.left+"+"+eventAdd.right+"="+eventAdd.res);
			valuation.put(eventAdd.res, valuate(eventAdd.left).add(valuate(eventAdd.right)));
			acknowledgeEvent(eventAdd);
		}
    }
    
    public void visit(ClientEventAssign eventAssign) {
	synchronized(valuation) {
		System.out.println("Assigning "+eventAssign.var+" to "+eventAssign.val);
	    valuation.put(eventAssign.var, eventAssign.val);
	    acknowledgeEvent(eventAssign);
	}
    }
    
    public void visit(ClientEventBeginAtomic _) {
	throw new UnsupportedOperationException("ClientEventBeginAtomic");
    }
    
    public void visit(ClientEventCompare eventCompare) {
	synchronized(valuation) {
	    valuation.put(eventCompare.res, BigInteger.valueOf(valuate(eventCompare.left).compareTo(valuate(eventCompare.right))));
	    acknowledgeEvent(eventCompare);
	}
    }
    
    /**
     * Connects a client given a connection event from the client. 
     * This is done
     * 
     * @param clientName
     * @param clientAddress
     */
    public void visit(ClientEventConnect eventConnect) {
	final String clientName = eventConnect.clientName;
	final InetSocketAddress clientAddress = eventConnect.clientAddress;
	PointToPointQueueSenderEndNonRobust<ClientEvent> queueToClient = new PointToPointQueueSenderEndNonRobust<ClientEvent>(); 
	queueToClient.setReceiver(clientAddress);
	synchronized(clients) {
	    clients.put(clientName,queueToClient);
	    acknowledgeEvent(eventConnect);
	}
    }
    
    /**
     * Disconnects a client on a disconnect event from the client. 
     * 
     * @param eventDisconnect
     */
    public void visit(ClientEventDisconnect eventDisconnect) {
	synchronized(clients) {
	    PointToPointQueueSenderEndNonRobust<ClientEvent> queueToClient = clients.remove(eventDisconnect.clientName);
	    queueToClient.shutdown();
	}
    }
    
    /**
     * Not supported yet.
     * @param _
     */
    public void visit(ClientEventEndAtomic _) {
	throw new UnsupportedOperationException("ClientEventEndAtomic");
    }
    
    /**
     * Multiplies to variables on a multiplication event from the client.
     * 
     * @param eventMult The multiplication event from the client.
     */
    public void visit(ClientEventMult eventMult) {
	synchronized(valuation) {
	    valuation.put(eventMult.res, valuate(eventMult.left).multiply(valuate(eventMult.right)));
	    acknowledgeEvent(eventMult);
	}
    }
    
    /**
     * Reads a variable on a read event from the client.
     * 
     * @param eventRead The read event from the client.
     */
    public void visit(ClientEventRead eventRead) {
		synchronized(valuation) {
			System.out.println("Read variable "+eventRead.var+" = "+valuate(eventRead.var));
			System.out.println("HashMap: "+valuation);
			eventRead.setVal(valuate(eventRead.var));
			acknowledgeEvent(eventRead);
		}
    }
    
    protected PointToPointQueueReceiverEndNonRobust<ClientEvent> operationsFromClients;
    
    /** 
     * Keeps getting the next operation from a server and then visits the operation.
     * Sub-classes have to implement the visiting methods.
     */
    public void run() {
		ClientEvent nextOperation;
		while ((nextOperation = operationsFromClients.get())!=null) {
			nextOperation.accept(this);
		}
    }

	public void visit(ClientEventConnectDenied event){
		System.out.println("Visitor of a denied connect of user "+event.clientName+".");
	}
	
	public HashMap<String,BigInteger> getVariableMap() {
		synchronized(valuation){
			return (HashMap<String,BigInteger>)valuation;
		}
	}
	
	public void setVariableMap(HashMap<String,BigInteger> vm) {
		synchronized(valuation){
			valuation.putAll(vm);
			System.out.println("After putting all: "+valuation);
		}
	}
    
}
