package exercise3;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class Node {
	private int id;
	private Node successor;
	private Node predecessor;
	
	
	public Node(){
		id = Helper.random();
	}
	
	public Node lookup(Key key){
		if(Helper.between(key.getId(), predecessor.id, id))
			return this;
		else
			return successor.lookup(key);
	}
}
