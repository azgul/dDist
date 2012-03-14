/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week6;

import java.math.BigInteger;
import java.util.HashMap;
import multicastqueue.Timestamp;
import replicated_calculator.ClientEvent;
import replicated_calculator.ClientEventVisitor;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class ClientEventVarMap extends ClientEvent {
	protected HashMap<String,BigInteger> valuation;
	
	public ClientEventVarMap(String clientName, long eventID, Timestamp t, HashMap<String,BigInteger> v){
		super(clientName,eventID,t);
		valuation = v;
	}
	
	public void accept(ClientEventVisitor visitor){
		visitor.visit(this);
	}
	
	public HashMap<String,BigInteger> getMap(){
		return valuation;
	}
}
