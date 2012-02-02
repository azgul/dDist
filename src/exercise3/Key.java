package exercise3;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public class Key {
	private int id;
	
	public Key(){
		id = Helper.random();
	}
	
	public int getId(){
		return id;
	}
}
