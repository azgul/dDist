package week2;

/**
 *
 * @author Martin
 */
public interface AccountInterface {
	public String getName();
	public double getBalance();
	public void deposit(double amount);
	public void withdraw(double amount);
}
