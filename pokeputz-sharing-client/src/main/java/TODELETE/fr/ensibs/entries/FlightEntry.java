package TODELETE.fr.ensibs.entries;


import java.util.Date;

import net.jini.core.entry.Entry;

public class FlightEntry implements Entry {
	
	/**
	 * SerielKey
	 */
	private static final long serialVersionUID = -6047766231629717277L;
	
	public Integer flightCode;
	public Date departureDate;
	public Integer departureAirportCode;
	public Integer arrivalAirportCode;
	public Integer flightPrice;
	
	public FlightEntry(Integer flightCode, Date departureDate, Integer departureAirportCode, Integer arrivalAirportCode, Integer flightPrice) {
		super();
		this.flightCode = flightCode;
		this.departureDate = departureDate;
		this.departureAirportCode = departureAirportCode;
		this.arrivalAirportCode = arrivalAirportCode;
		this.flightPrice = flightPrice;
	}

	public FlightEntry() {
		super();
	}
	
	
	
}
