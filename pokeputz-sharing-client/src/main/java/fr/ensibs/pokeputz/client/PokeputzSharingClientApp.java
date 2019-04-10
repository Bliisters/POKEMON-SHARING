package fr.ensibs.pokeputz.client;

import java.util.Scanner;

import fr.ensibs.pokeputz.common.Farmer;

public class PokeputzSharingClientApp {
	
	Controller controller;
	Farmer farmer;
	String host;
	int port;
	
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
			
			PokeputzSharingClientApp mainApp = new PokeputzSharingClientApp(host, port); // Creating a new instance of the app, calling constructor...
			mainApp.run(); // And running it.
		}
	}
	
	public PokeputzSharingClientApp(String host, int port)
	{
		this.controller = new Controller(host, port);
		this.host=host;
		this.port=port;
	}
	
	private static void help()
	{
		System.out.println("Lancez l'application avec les paramètres [host] [port] pour accéder au service d'échange de Pokeputz");
	}
	
	private static void usage()
	{
		System.out.println("Avant de commencer, connectez-vous grâce à la commande : ");
		System.out.println("CONNECT [username] [password]");
		
	}
	
	/**
	 * Application start. Proceeds reading on standard input stream.
	 */
	public void run() {
		usage(); // Prints the usable commands 
		
	    Scanner scanner = new Scanner(System.in);
	    String line = scanner.nextLine();
	    while (!(line.toUpperCase()).equals("QUIT")) { //Start scanning input for instructions
	    	
	      String[] command = line.split(" +");
	      
		  while(this.farmer==null) 
		  {
			  if(!command[0].toUpperCase().equals("CONNECT"));
			  usage();
			  line = scanner.nextLine();
		  }
	    	  
	    
	    	   
	      switch (command[0].toUpperCase()) { 
	      
	        case "CONNECT":
	        	controller.connect();
	        break;
	        
	        case "LIST":
	        	controller.list();
	        break;
	        
	        case "REPLY":
	        	controller.reply();
	        break;
	        
	        case "NOTIFY":
	        	controller.notify();
	        break;

	        
	        default:
	        	System.err.println("Unknown command: \"" + command[0] + "\"");
	      }
	      line = scanner.nextLine();
	    }
	    scanner.close();
	}

}
