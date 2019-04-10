package fr.ensibs.pokeputz.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import fr.ensibs.pokeputz.authentification.Authentifier;
import fr.ensibs.pokeputz.publisher.AdvertisementSharingServer;
import fr.ensibs.pokeputz.publisher.Publisher;
import fr.ensibs.pokeputz.util.DatabaseCRUD;

public class PokeputSharingServerApp {

	private static final String REMOTE_AUTHENTIFIER = "AUTHENTIFIER";
	private static final String REMOTE_PUBLISHER = "PUBLISHER";
	
	String host;
	int port;

	Authentifier auth;
	Publisher publish;
	//TradingController trade;
	DatabaseCRUD CRUD;
	
	public static void main(String[] args) {
		
		if (args.length < 4 || args[0].equals("-h")) //check arguments, print usage if incorrect or "-h"
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
			String dbuser = args[2];
			String dbport = args[3];
			
			PokeputSharingServerApp mainApp = new PokeputSharingServerApp(host, port, dbuser, dbport); // Creating a new instance of the app, calling constructor...
			mainApp.run(); // And running it.
		}
	}


	/**
	 * Constructor
	 * @param host The server hosting the application. Use IP, name of machine or localhost
	 * @param port The port on which the server is listening
	 */
	public PokeputSharingServerApp(String host, int port, String dbuser, String dbpass) {
		this.host = host;
		this.port = port;

		this.CRUD = new DatabaseCRUD(dbuser, dbpass);
		
		try {
			this.publish = new Publisher(this.CRUD, new AdvertisementSharingServer(host, port));
		} catch (Exception e) {
			System.err.println("Unable to start JMS service. Out.");
			e.printStackTrace();
			System.exit(1);
		}
		
		this.auth = new Authentifier(dbuser, dbpass, this.CRUD);
		try {
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind( REMOTE_AUTHENTIFIER , this.auth);
			registry.bind( REMOTE_PUBLISHER , this.publish);
		} catch (Exception e) {
			System.err.println("Unable to start RMI service. Out.");
			e.printStackTrace();
			System.exit(1);
		}
		

		
		//this.trade = new TradingController(host, port+2);
		
	}
	
	/**
	 * Usage shown when using the -h option or when arguments are incorrects.
	 * Print the string on system standard output.
	 */
	public static void usage(){
	    System.out.println("Usage: java -jar Pokeputz-sharing-server-1.0.jar <server_host> <server_port> <Database_username> <Database_pass>");
	    System.out.println("Launch the server flight reservation application");
	    System.out.println("with:");
	    System.out.println("<server_host>  the name of the server host");
	    System.out.println("<server_port>  the first port where the server is launched. three ports will be used.");
	    System.out.println("<Database_username>  the name of the database user to use with the app");
	    System.out.println("<Database_pass>  the password for the database user.");
	    System.exit(0);
	}
	
	/**
	 * Usage shown when using the application is reading system input.
	 * Print the string on system standard output.
	 */
	public static void readingUsage(){
	    System.out.println("Connected. Enter commands:"
	    	    + "\n ADDFARMER <farmer_name> <farmer_pass>"
	    		+ "\n	- To create a new farmer instance."
	    	    + "\n ADDPOKEPUT <pokeput_name> <pokeput_evol_token> <pokeput_type1> <pokeput_type2>"
	    		+ "\n	- To create a new pokeput instance. Evol and types can be NULL"
	    	    + "\n GIVEPOKEPUT <pokeput_token> <farmer_token>"
	    		+ "\n	- To add a pokeput to a farmer");
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
	        case "ADDFARMER":
	        	this.addfarmerinstruction(command);
	        break;
	        
	        case "ADDPOKEPUT":
	        	this.addpokeputinstruction(command);
	        break;
	        
	        case "GIVEPOKEPUT":
	        	this.givepokeputinstruction(command);
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


	private void quit() {
	}


	private void givepokeputinstruction(String[] command) {
		
	}


	private void addpokeputinstruction(String[] command) {
		
	}


	private void addfarmerinstruction(String[] command) {
        if (command.length >= 3 && !command[1].equals("-h")) { // Checking arguments
        	String farmername = command[1];
        	String farmerpass = command[2];
        	
        	if (farmername == null || farmerpass == null) {
        		System.out.println("Operation cancelled");
        		return;
        	}
        	
        	try {
				auth.signIn(farmername, farmerpass);
				System.out.println("Farmer sucessfully added.");
			} catch (Exception e) {
				System.out.println("Unable to create the new farmer ["+farmername+"]" );
				e.printStackTrace();
			}
        }
        else
        	System.out.println("Usage: ADDFARMER <farmer_name> <farmer_pass>");
	}
	
}
