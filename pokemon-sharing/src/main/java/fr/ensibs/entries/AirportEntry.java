package fr.ensibs.entries;

import net.jini.core.entry.Entry;

public class AirportEntry implements Entry {

	/**
	 * SerialKey
	 */
	private static final long serialVersionUID = -35573849843344342L;

	public Integer airportCode;
	public String cityName;
	
	public AirportEntry(Integer airportCode, String cityName) {
		super();
		this.airportCode = (airportCode);
		this.cityName = (cityName);
	}

	public AirportEntry() {
		super();
	}

}
