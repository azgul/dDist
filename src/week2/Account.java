package week2;

/**
 *
 * @author Martin
 */
public class Account implements AccountInterface {
	private String name;
	private double balance;
	
	public Account(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void deposit(double amount) {
		balance += amount;
	}
	
	public void withdraw(double amount) {
		balance -= amount;
	}
}
