package TODELETE.fr.ensibs.flightapp;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

import TODELETE.fr.ensibs.entries.AccountEntry;
import TODELETE.fr.ensibs.entries.AirportEntry;
import TODELETE.fr.ensibs.entries.FlightEntry;
import TODELETE.fr.ensibs.entries.SeatEntry;
import fr.ensibs.river.RiverLookup;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 * Checker class used to check formats and type of arguments
 * @author Robin ASPE, Quentin FLEGEAU
 *
 */
public class Checker {

	/**
	 * A javaspace to check if templates are correct or entries already exists
	 */
	JavaSpace space;
	
	/**
	 * Constructor
	 * @param space A javaspace given to check if templates are correct 
	 */
	public Checker(JavaSpace space) {
		super();
		this.space = space;
	}
	
	/**
	 * A method checking if a template returns values from a javaspace
	 * @param template the template to check
	 * @return True if a value is returned, False if not
	 */
	public boolean check(Entry template) {
		try {
			if(space.readIfExists(template, null, Lease.FOREVER) != null) {
				return true;
			}
			else
			{
				return false;
			}
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * A method checking if an Airport Entry with the same code or City Name exists
	 * @param entry The new entry to write in the javaspace
	 * @return True if it already exists, False and a text print in System.out if not.
	 */
	public boolean exists(AirportEntry entry) {
		if (check(new AirportEntry(entry.airportCode, null))) {
			System.out.println("Airport code ["+entry.airportCode+"] already exists.");
			return true;
		} else if (check(new AirportEntry(null, entry.cityName))) {
			System.out.println("Airport city name ["+entry.cityName+"]already exists.");
			return true;
		} else
			return false;
	}
	
	/**
	 * A method checking if a Flight Entry with the same code exists
	 * @param entry The new entry to write in the javaspace
	 * @return True if it already exists, False if not
	 */
	public boolean exists(FlightEntry entry) {
		if (check(new FlightEntry(entry.flightCode, null,null,null,null)))
		{
			System.out.println("Flight code ["+entry.flightCode+"] already exists.");
			return true;
		} else
			return false;
	}
	
	/**
	 * A method checking if a Seat Entry with the same code or City Name exists
	 * @param entry The new entry to write in the javaspace
	 * @return True if it already exists, False and a text print in System.out if not.
	 */
	public boolean exists(SeatEntry entry) {
		if (check(new SeatEntry(entry.flightCode, entry.seatNumber, null))) {
			System.out.println("Seat ["+entry.seatNumber+"] on flight ["+entry.flightCode+"] already exists.");
			return true;
		} else
			return false;
	}
	
	/**
	 * A method checking if an Account Entry with the same number exists 
	 * @param entry The new entry to write in the javaspace
	 * @return True if it already exists, False and a text print in System.out if not.
	 */
	public boolean exists(AccountEntry entry) {
		if (check(new AccountEntry(entry.account_number, null))) {
			System.out.println("Account ["+entry.account_number+"] already exists.");
			return true;
		} else
			return false;
	}
	
	
	/**
	 * A method checking a string can correctly be parsed into an Integer
	 * @param str The string to parse
	 * @return the parsed Integer, or null if the string cannot be parsed, with a text on System.out
	 */
	public Integer returnIfInteger(String str) {
    	Integer result = 0;
    	try {
    		result = Integer.parseInt(str);
    	}
    	catch (Exception e) {
    		System.out.println("Argument ["+str+"] not recognised as a number.");
    		result = null;
    	}
    	return result;
	}	
	
}
