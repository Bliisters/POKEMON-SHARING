package fr.ensibs.pokeputz.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;

import fr.ensibs.pokeputz.authentification.AuthentifierClient;
import fr.ensibs.pokeputz.common.Farmer;
import fr.ensibs.pokeputz.common.PokePost;
import fr.ensibs.pokeputz.common.Pokeput;
import fr.ensibs.pokeputz.interfaces.IAuthentifier;
import fr.ensibs.pokeputz.interfaces.IPublisher;

public class PokeputzSharingClientApp {

	private static final String REMOTE_AUTHENTIFIER = "AUTHENTIFIER";
	private static final String REMOTE_PUBLISHER = "PUBLISHER";
	  
	private Farmer farmer;
	IAuthentifier auth;
	IPublisher publish;
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
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
			
			PokeputzSharingClientApp mainApp = new PokeputzSharingClientApp(host, port); // Creating a new instance of the app, calling constructor...
			mainApp.run(); // And running it.
		}
	}
	
	public PokeputzSharingClientApp(String host, int port) throws RemoteException, NotBoundException
	{
		Registry registry = LocateRegistry.getRegistry(host, port);
		IAuthentifier authentifier = (IAuthentifier) registry.lookup(REMOTE_AUTHENTIFIER);
		IPublisher publisher = (IPublisher) registry.lookup(REMOTE_PUBLISHER);
		this.auth = authentifier;
		this.publish = publisher;
		
	}
	
	private static void usage()
	{
	    System.out.println("Usage: java -jar Pokeputz-sharing-client-1.0.jar <server_host> <server_port>");
	    System.out.println("Launch the client pokeputz trading application");
	    System.out.println("with:");
	    System.out.println("<server_host>  the name of the server host");
	    System.out.println("<server_port>  the first port where the server is launched.");
		
	}
	
	/**
	 * Usage shown when using the application is reading system input.
	 * Print the string on system standard output.
	 */
	public void readingUsage(){
		if (this.farmer == null)
		{
	    System.out.println("Connected. Enter commands:"
	    	    + "\n LOGIN <username> <password>"
	    		+ "\n	- To log in the system as a new farmer."
	    	    + "\n QUIT "
	    		+ "\n	- To resign." );
		}
		else
		{
		    System.out.println("Connected as "+farmer.getName()+". Enter commands:"
		    	    + "\n MYPOKEPUTS"
		    		+ "\n	- To list your pokeputs."
		    	    + "\n LISTOFFERS"
		    		+ "\n	- To display the list of available offers"
		    	    + "\n POSTOFFER <tags>"
		    		+ "\n	- To post an exchange offer"
		    	    + "\n SUBSCRIPT <tags>"
		    		+ "\n	- To subscript to offers"
		    	    + "\n ANSWEROFFER <Offer_Token>"
		    		+ "\n	- To answer to an offer to trade pokeputz"
		    	    + "\n LOGOUT"
		    		+ "\n	- To log out."
		    	    + "\n QUIT "
		    		+ "\n	- To resign.");
		}
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
	      
		  if(this.farmer==null) 
		  {
			  if(command[0].toUpperCase().equals("LOGIN"))
			  {
				  try {
				  this.farmer = auth.login(command[1], DigestUtils.sha256Hex(command[2]));
				  }
				  catch (Exception e) {
					  
				  }
				  
				  if (farmer == null)
					  System.out.println("Login failed");
				  else
					  System.out.println("Now connected as "+farmer.getName());
					  
			  }
			  else
				  usage();
			  line = scanner.nextLine();
		  }
		  else  
		      switch (command[0].toUpperCase()) { 
		      	        
		        case "LOGOUT":
		        	this.farmer = null;
					System.out.println("Logged out.");
		        break;
		        
		        case "MYPOKEPUTS":
					System.out.println("List of your POKEPUTZ: ");
					for (Pokeput p : farmer.getPokeputz())
					{
						System.out.println(p.toString());
					}
		        break;
		        
		        case "LISTOFFERS":
					System.out.println("List of available offers: ");
				try {
					for (PokePost p : publish.consultPosts())
					{
						System.out.println(p.toString());
					}
				} catch (SQLException e) {
					System.out.println("Could not find list of offers.");
					e.printStackTrace();
				}
		        break;
		        
		        case "POSTOFFER":
		        	// TODO
		        break;
	
		        
		        default:
		        	System.err.println("Unknown command: \"" + command[0] + "\"");
	      }
	      line = scanner.nextLine();
	    }
	    scanner.close();
	}

}
