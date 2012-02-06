package week2;

import java.util.*;

/**
 *
 * @author Martin
 */
public class Bank implements BankInterface {
	private HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	public Account getAccount (String name) {
		Account account = accounts.get(name);
		if (account == null)
			account = createAccount(name);
		
		return account;
	}
	
	private Account createAccount (String name) {
		Account account = new Account(name);
		accounts.put(name, account);
		return account;
	}
}
