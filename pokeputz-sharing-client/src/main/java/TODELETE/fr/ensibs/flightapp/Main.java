package TODELETE.fr.ensibs.flightapp;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import TODELETE.fr.ensibs.entries.AccountEntry;
import TODELETE.fr.ensibs.entries.AirportEntry;
import TODELETE.fr.ensibs.entries.FlightEntry;
import TODELETE.fr.ensibs.entries.SeatEntry;
import fr.ensibs.river.RiverLookup;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.transaction.CannotAbortException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.server.ServerTransaction;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;


/**
 * Main class of the flight monitoring application.
 * @author Robin ASPE, Quentin FLEGEAU
 *
 */
public class Main {

	/**
	 * Utilitary used to find the javaspace and javaspace entries
	 */
	RiverLookup finder; 
	/**
	 * The Javaspace Object where are stored entries of airports, flights, seats and payments
	 */
	JavaSpace space;
	/**
	 * The format for dates used in the whole application: DD-MM-YYYY
	 */
	SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-Y");
	/**
	 *  The class used to check all parameters and asserts
	 */
	Checker checker; 
	/**
	 *  The port of the Javaspace server
	 */
	int port;
	/**
	 *  The hostname of the Javaspace server
	 */
	String host;
	
	/**
	 * Main method of main class - Entrance of application
	 * @param args Arguments given at launch: [-h] for usage or [server_host] [server_port]
	 */
	public static void main(String[] args) {
		if (args.length < 2 || args[0].equals("-h")) //check arguments, print usage if incorrect or "-h"
			usage();
		else 
		{
			String host = args[0];
			int port = 0;
			try {
				port = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e) {
				System.err.println("Host port not recognised as a number.");
				System.exit(1);
			}
			
			Main mainApp = new Main(host, port); // Creating a new instance of the app, calling constructor...
			mainApp.run(); // And running it.
		}
	}
	
	/**
	 * Constructor
	 * @param host The server hosting the application. Use IP, name of machine or localhost
	 * @param port The port on which the server is listening
	 */
	public Main(String host, int port) {
		try {
			finder = new RiverLookup(); // Create a lookup for the javaspace.
		} catch (Exception e) {
			System.err.println("Unable to create a RiverLookup: ");
			e.printStackTrace();
			System.exit(1);
		}
        try {
			space = (JavaSpace) finder.lookup(host, port, JavaSpace.class); // And look for a javaspace on the server coordinates
		} catch (Exception e) {
			System.err.println("Unable to find the Javaspace: Is the server running? Check host and port.");
			e.printStackTrace();
			System.exit(1);
		}
        checker = new Checker(space); // Create the checker
        this.host=host;
        this.port=port;
	}
	
	/**
	 * Usage shown when using the -h option or when arguments are incorrects.
	 * Print the string on system standard output.
	 */
	public static void usage(){
	    System.out.println("Usage: java FlightReservation <server_host> <server_port>");
	    System.out.println("Launch the client flight reservation application");
	    System.out.println("with:");
	    System.out.println("<server_host>  the name of the flight monitoring javaspace server host");
	    System.out.println("<server_port>  the port where the server is launched");
	    System.exit(0);
	}
	
	/**
	 * Usage shown when using the application is reading system input.
	 * Print the string on system standard output.
	 */
	public static void readingUsage(){
	    System.out.println("Connected. Enter commands:"
	    	    + "\n ADD AIRPORT <airport_code> <city_name>"
	    		+ "\n	- To create a new airport instance."
	    	    + "\n ADD FLIGHT <flight_code> <departure_date (DD-MM-YYYY) > <departure_airport_code> <arrival_airport_code> <flight_price> "
	    		+ "\n	- To create a new flight instance."
	    	    + "\n ADD SEAT <seat_number> <flight_code>"
	    		+ "\n	- To create a new seat instance, free when created."
	    	    + "\n ADD ACCOUNT <account_number> <account_balance>"
	    	    + "\n 	- To create a new account instance."
	    	    + "\n CHECK ACCOUNT <account_number>"
	    	    + "\n 	- To check an account instance. Use '*' as a joker value."
	    	    + "\n LOOKFOR <date> <start_city> <arrival_city>"
	    		+ "\n	- To look for a flight code. Use '*' as a joker value."
	    	    + "\n RESERVE <flight_code> <account_number>"
	    	    + "\n 	- Reserve a free seat from a flight code"
	    	    + "\n HELP"
	    	    + "\n 	- Print this message"
	    	    + "\n QUIT"
	    	    + "\n 	- Quit application.");
	}
	
	/**
	 * Application start. Proceeds reading on standard input stream.
	 */
	public void run() {
		readingUsage(); // Prints the usable commands 
		
	    Scanner scanner = new Scanner(System.in);
	    String line = scanner.nextLine();
	    while (!(line.toUpperCase()).equals("QUIT")) { //Start scanning input for instructions
	    	
	      String[] command = line.split(" +");
	      
	      switch (command[0].toUpperCase()) { 
	      // ADDING methods
	        case "ADD":
	        	this.addInstructions(command);
	        break;
	        
	        case "CHECK":
	        	this.checkInstructions(command);
	        break;
	        
	        case "LOOKFOR":
	        	this.lookforInstruction(command);
	        break;
	        
	        case "RESERVE":
	        	this.reserveInstruction(command);
	        break;
	        
	        case "HELP":
	        	readingUsage();
	        break;
	        
	        default:
	        	System.err.println("Unknown command: \"" + command[0] + "\"");
	      }
	      line = scanner.nextLine();
	    }
	    scanner.close();
	    quit();
	}
	
	/**
	 * Use a string array to run appropriate ADDING methods
	 * @param command The String array containing the method to call and the arguments, read by a scanner.
	 */
	public void addInstructions(String[] command)
	{
    	
	    if (command.length >= 2 && !command[1].equals("-h")) {
		      switch (command[1].toUpperCase()) { 
		      // ADDING AIRPORT
	        case "AIRPORT":
	        	this.addAirportInstruction(command);
	        break;
	        // ADDING FLIGHT
	        case "FLIGHT":
	        	this.addFlightInstruction(command);
	        break;
	        // ADDING SEAT
	        case "SEAT":
	        	this.addSeatInstruction(command);
	        break;
	     // ADDING ACCOUNT
	        case "ACCOUNT":
	        	this.addAccountInstruction(command);
	        break;
	    	default:
	    	// If arguments are not recognised, print usage of ADD instruction
		        System.err.println("Bad argument: \"" + command[1] + "\"");
		        System.out.println("Usage: ADD <AIRPORT|FLIGHT|SEAT|ACCOUNT>");
		      }
	    } else {
	    	// If arguments are missing or -h , print usage of ADD instruction
	    	System.out.println("Usage: ADD <AIRPORT|FLIGHT|SEAT|ACCOUNT>");
	    }
	}
	
	/**
	 * Use a string array to run appropriate CHECKING methods
	 * @param command The String array containing the method to call and the arguments, read by a scanner.
	 */
	public void checkInstructions(String[] command)
	{
    	
	    if (command.length >= 2 && !command[1].equals("-h")) {
		      switch (command[1].toUpperCase()) { 
	     // CHECKING ACCOUNT
	        case "ACCOUNT":
	        	this.checkAccountInstruction(command);
	        break;
	    	default:
	    	// If arguments are not recognised, print usage of CHECK instruction
		        System.err.println("Bad argument: \"" + command[1] + "\"");
		        System.out.println("Usage: CHECK <ACCOUNT>");
		      }
	    } else {
	    	// If arguments are missing or -h , print usage of ADD instruction
	    	System.out.println("Usage: CHECK <ACCOUNT>");
	    }
	}
	
	/**
	 * Add an account entry to the javaspace with arguments given in a string array
	 * @param command String array containing the account parameters (ADD ACCOUNT account_number account_balance)
	 */
	public void addAccountInstruction (String[] command){

        if (command.length >= 4 && !command[2].equals("-h")) { // Checking arguments
        	
        	Integer account_number = checker.returnIfInteger(command[2]);
        	
        	Integer account_balance = checker.returnIfInteger(command[3]);
        	
        	if (account_number == null || account_balance == null) {
        		System.out.println("Operation cancelled");
        		return;
        	}
        	
        	AccountEntry account = new AccountEntry(account_number, account_balance);
        	if (checker.exists(account))
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	try {
				space.write(account, null, Lease.FOREVER);
				System.out.println("Account ["+account_number+"] sucessfully added.");
			} catch (RemoteException | TransactionException e) {
				System.out.println("Unable to create the new account ["+account_number+","+account_balance+"]" );
				e.printStackTrace();
			}
        }
        else
        	System.out.println("Usage: ADD ACCOUNT <account_number> <account_balance>");
	}
	
	/**
	 * Check for an Account informations from the javaspace with search criteria given in a string array
	 * @param command String array containing the account search parameters (CHECK ACCOUNT account_number). Any field can be filled with '*', which is the joker argument.
	 */
	public void checkAccountInstruction (String[] command){

        if (command.length >= 3 && !command[2].equals("-h")) { // Checking arguments
        	
        	AccountEntry templateAccount = null;
        	
    		if (command[2].equals("*")) { // Check if this argument is joker
    			templateAccount = new AccountEntry(null, null);
        	}
    		else {
            	Integer account_number = checker.returnIfInteger(command[2]);
            	
            	if (account_number == null) {
            		System.out.println("Operation cancelled");
            		return;
            	}
            	
            	templateAccount = new AccountEntry(account_number, null);
    		}
        
    		AccountEntry resultAccount = null;
			try {
				resultAccount = (AccountEntry) space.readIfExists(templateAccount, null, Lease.FOREVER);
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}
			
			if (resultAccount == null)
			{
				System.out.println("Account ["+command[2]+"] not found");
			}
			else
			{
				int result = resultAccount.account_number;
				System.out.println("Account number ["+result+"] has a balance of ["+resultAccount.account_balance+"]");
			}
        }
        else
        	System.out.println("Usage: CHECK ACCOUNT <account_number>");
	}
	
	/**
	 * Add an airport entry to the javaspace with arguments given in a string array
	 * @param command String array containing the airport parameters (ADD AIRPORT airport_code city_name)
	 */
	public void addAirportInstruction (String[] command){

        if (command.length >= 4 && !command[2].equals("-h")) { // Checking arguments
        	
        	Integer airport_code = checker.returnIfInteger(command[2]);
        	
        	String city_name = command[3];
        	
        	if (airport_code == null || city_name == null) {
        		System.out.println("Operation cancelled");
        		return;
        	}
        	
        	AirportEntry airport = new AirportEntry(airport_code, city_name);
        	if (checker.exists(airport))
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	try {
				space.write(airport, null, Lease.FOREVER);
				System.out.println("Airport sucessfully added.");
			} catch (RemoteException | TransactionException e) {
				System.out.println("Unable to create the new airport ["+airport_code+","+city_name+"]" );
				e.printStackTrace();
			}
        }
        else
        	System.out.println("Usage: ADD AIRPORT <airport_code> <city_name>");
	}
	
	
	/**
	 * Add a Flight entry to the javaspace with arguments given in a string array
	 * @param command String array containing the flight parameters (ADD FLIGHT flight_code departure_date(DD-MM-YYYY) departure_airport_code arrival_airport_code flight_price)
	 */
	public void addFlightInstruction (String[] command){

        if (command.length >= 7 && !command[2].equals("-h")) { //Checking arguments
        	Integer flightCode = checker.returnIfInteger(command[2]);
        	
        	Date departureDate = null;
			try {
				departureDate = dateFormat.parse(command[3]);
			} catch (ParseException e1) {
				System.out.println("Wrong date format: \""+command[3]+"\" found while \"DD-MM-YYYY\" was expected. Operation cancelled.");
				e1.printStackTrace();
				return;
			}
			
        	Integer departureAirportCode = checker.returnIfInteger(command[4]);
        	
        	Integer arrivalAirportCode = checker.returnIfInteger(command[5]);
        	
        	Integer flightPrice = checker.returnIfInteger(command[6]);
        	
        	if (	flightCode == null 
        		|| 	departureDate == null 
        		|| 	departureAirportCode == null 
        		|| 	arrivalAirportCode == null 
        		|| 	flightPrice == null) 
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	
        	FlightEntry flight = new FlightEntry(flightCode, departureDate, departureAirportCode, arrivalAirportCode, flightPrice);
        	if (checker.exists(flight))
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	try {
				space.write(flight, null, Lease.FOREVER);
				System.out.println("Flight sucessfully added.");
			} catch (RemoteException | TransactionException e) {
				System.out.println("Unable to create the new flight ["+flightCode+","+dateFormat.format(departureDate)+","+departureAirportCode+","+arrivalAirportCode+","+flightPrice+"]" );
				e.printStackTrace();
			}
        }
        else
        	System.out.println("Usage: ADD FLIGHT <flight_code> <departure_date (DD-MM-YYYY)> <departure_airport_code> <arrival_airport_code> <flight_price>");
    	
	}
	
	/**
	 * Add a seat entry to the javaspace with arguments given in a string array
	 * @param command String array containing the airport parameters (ADD SEAT seat_number flight_code)
	 */
	public void addSeatInstruction (String[] command) {

        if (command.length >= 4 && !command[2].equals("-h")) {
        	Integer seat_number = checker.returnIfInteger(command[2]);;
        	Integer flight_code = checker.returnIfInteger(command[3]);;
        	
        	if (	seat_number == null 
	        		|| 	flight_code == null )
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	
        	SeatEntry seat = new SeatEntry(seat_number, flight_code, false); // create the new seat, free by default
        	if (checker.exists(seat))
        	{
        		System.out.println("Operation cancelled");
        		return;
        	}
        	try {
				space.write(seat, null, Lease.FOREVER);
				System.out.println("Seat sucessfully added.");
			} catch (RemoteException | TransactionException e) {
				System.out.println("Unable to create the new seat ["+seat_number+","+flight_code+"]" );
				e.printStackTrace();
			}
        }
        else
        	System.out.println("Usage: ADD SEAT <seat_number> <flight_code>");
    	
	}
	
	
	/**
	 * Look for a Flight code from the javaspace with search criteria given in a string array
	 * @param command String array containing the flight search parameters (LOOKFOR date start_city arrival_city). Any field can be filled with '*', which is the joker argument.
	 */
	public void lookforInstruction(String[] command) {

    	if (command.length >= 4 && !command[1].equals("-h")) //Check arguments
    	{
    		Date date = null;
    		if (command[1].equals("*"))
    		{
    			
    		}
    		else
    		{
        		try {
					date = dateFormat.parse(command[1]);
				} catch (ParseException e) {
					System.err.println("Wrong date format: \""+command[3]+"\" found while \"DD-MM-YYYY\" was expected. Operation cancelled.");
					e.printStackTrace();
					return;
				}
    		}
    		
    		String startCity = command[2];
    		String arrivalCity = command[3];
    		
    		AirportEntry templateAirport = new AirportEntry(null, startCity);
    		AirportEntry resultAirport = null;
    		
    		if (startCity.equals("*")) // Check if this argument is joker
    		{}
    		else
    		{
				try {
					resultAirport = (AirportEntry) space.readIfExists(templateAirport, null, Lease.FOREVER);
				} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
					System.out.println("Unable to find airport in " + startCity + ". Opération cancelled.");
					e.printStackTrace();
					return;
				}
    		}
			
			Integer startCode = null;
			if (resultAirport != null)
			{
				startCode = resultAirport.airportCode;
			}
    		
    		if (startCity.equals("*")) // Check if this argument is joker
    		{}
    		else
    		{
        		templateAirport.cityName = arrivalCity;
        		try {
					resultAirport = (AirportEntry) space.readIfExists(templateAirport, null, Lease.FOREVER);
				} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
					System.err.println("Unable to find airport in " + arrivalCity + ". Opération cancelled." );
					e.printStackTrace();
					return;
				}
    		}
    		
    		Integer arrivalCode = null;
			if (resultAirport != null)
			{
				arrivalCode = resultAirport.airportCode;
			}
    		
			// Look for an entry matching arguments
    		FlightEntry templateFlight = new FlightEntry(null, date, startCode, arrivalCode, null);
    		
    		FlightEntry resultFlight = null;
			try {
				resultFlight = (FlightEntry) space.readIfExists(templateFlight, null, Lease.FOREVER);
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}
			
			if (resultFlight == null)
			{
				System.out.println("No flight corresponding  for the "+dateFormat.format(date)+" from "+ startCity +" to "+ arrivalCity);
			}
			else
			{
				int result = resultFlight.flightCode;
				System.out.println("Flight number ["+result+"] is due the " +dateFormat.format(resultFlight.departureDate)+ " from "+startCity+" to "+arrivalCity+ ". Price: "+ resultFlight.flightPrice);
			}
    	}
    	else
    	{
	          System.out.println("Usage: LOOKFOR <date> <start_city> <arrival_city> \n"
	          		+ "Use '*' as a joker value.");
    	}
    	
	}
	
	/**
	 * Reserve a seat into the javaspace from a flight code and an account number.
	 * @param command String array containing the flight code and the account number (RESERVE flight_code account_number).
	 */
	public void reserveInstruction(String[] command) {

    	if (command.length >= 3 && !command[1].equals("-h"))
    	{
    		Integer flightCode = checker.returnIfInteger(command[1]);
    		
    		Integer account_number = checker.returnIfInteger(command[2]);
    		
        	if (	flightCode == null || account_number == null )
	        	{
	        		System.out.println("Operation cancelled");
	        		return;
	        	}
        	
        	Transaction t = null;
        	try {
        		TransactionManager transactionManager = finder.lookup(host, port, TransactionManager.class);
				long transactionID = transactionManager.create(60000).id;
				t = new ServerTransaction(transactionManager,transactionID);
			} catch (Exception e) {
				System.err.println("Unable to create a Transaction / TransactionManager");
				e.printStackTrace();
				return;
			}
        	
        	
    		SeatEntry templateSeat = new SeatEntry(null, flightCode, false);
    		SeatEntry resultSeat = null;
    		
    		// Remove the existing seat from the javaspace
			try {
				resultSeat = (SeatEntry) space.takeIfExists(templateSeat, t, Lease.FOREVER);
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				System.err.println("Unable to find a free seat in flight ["+ flightCode +"]");
				e.printStackTrace();
			}
			
			FlightEntry templateFlight = new FlightEntry(flightCode, null, null, null, null);
			FlightEntry resultFlight = null;
    		
    		// Read the existing flight from the javaspace
			try {
				resultFlight = (FlightEntry) space.readIfExists(templateFlight, t, Lease.FOREVER);
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				System.err.println("Unable to find the flight ["+ flightCode +"]");
				e.printStackTrace();
			}
			
			AccountEntry templateAccount = new AccountEntry(account_number, null);
			AccountEntry resultAccount = null;
			
			// Remove the existing account from the javaspace
			try {
				resultAccount = (AccountEntry) space.takeIfExists(templateAccount, t, Lease.FOREVER);
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				System.err.println("Unable to find account ["+ account_number +"]");
				e.printStackTrace();
			}
			
			// Check if such seat exists
			if (resultSeat != null)
			{
				// Check if the flight exists
				if (resultFlight != null)
				{
					// Check the account exists
					if (resultAccount != null)
					{
						// Take price value
						Integer flightPrice = resultFlight.flightPrice;
						// Take account balance
						Integer account_balance = resultAccount.account_balance;
						if(flightPrice <= account_balance) {
							// Then modifies it's reserved value to TRUE
							resultSeat.reserved = true;
							// Then modifies the account balance
							resultAccount.account_balance=account_balance-flightPrice;
							try {
								// Finally, write them again into the space
								space.write(resultSeat, t, Lease.FOREVER);
								space.write(resultAccount, t, Lease.FOREVER);
								t.commit();
								System.out.println("Seat number ["+resultSeat.seatNumber+"] was sucessfully reserved." );
								System.out.println("Account balance before : ["+account_balance+"]." );
								System.out.println("Flight price : ["+flightPrice+"]." );
								System.out.println("Current account balance : ["+resultAccount.account_balance+"]." );
							} catch (RemoteException | TransactionException e) {
								System.err.println("Unable to reserve the new seat" );
								e.printStackTrace();
							}
						}
						else {
							try {
								t.abort();
							} catch (UnknownTransactionException | CannotAbortException | RemoteException e) {
								e.printStackTrace();
							}
							System.out.println("An error as occured with the account ["+account_number+"]. Not enought money." );
							System.out.println("Account balance : ["+account_balance+"]." );
							System.out.println("Flight price : ["+flightPrice+"]." );
						}
			        	
					}
					else {
						System.out.println("An error as occured with the account ["+account_number+"]. Maybe it does not exist." );
					}
				}
				else {
					System.out.println("An error as occured with the flight ["+flightCode+"]. Sorry for the inconvenience." );
				}
			}
			else {
				System.out.println("No free seats available for flight ["+flightCode+"]. Sorry for the inconvenience." );
			}
    	}
    	else{
    		System.out.println("Usage: RESERVE <flight_code> <account_number>");
    	}
	}
	

	private void quit() {
		// OnQuit methods go here
	}

}
