package TODELETE.fr.ensibs.entries;

import net.jini.core.entry.Entry;

public class AccountEntry implements Entry {

	/**
	 * SerialKey
	 */
	private static final long serialVersionUID = -3557389843344342L;

	public Integer account_number;
	public Integer account_balance;
	
	public AccountEntry(Integer account_number, Integer account_balance) {
		super();
		this.account_number = (account_number);
		this.account_balance = (account_balance);
	}

	public AccountEntry() {
		super();
	}

}

