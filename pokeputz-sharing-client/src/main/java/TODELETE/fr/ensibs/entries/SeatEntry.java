package TODELETE.fr.ensibs.entries;

import net.jini.core.entry.Entry;

public class SeatEntry implements Entry {

	/**
	 * SerialKey
	 */
	private static final long serialVersionUID = 5332339662249178L;
	
	public Integer seatNumber;
	public Integer flightCode;
	public Boolean reserved;
	
	public SeatEntry(Integer seatNumber, Integer flightCode, Boolean reserved) {
		super();
		this.seatNumber = seatNumber;
		this.flightCode = flightCode;
		this.reserved = reserved;
	}

	public SeatEntry() {
		super();
	}
	
	
	
}
