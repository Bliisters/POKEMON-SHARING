package fr.ensibs.pokeputz.main;

import java.util.Scanner;

import fr.ensibs.pokeputz.common.Farmer;

public class Application {
	
	
	public static void main(String[] args) {
		
		Farmer farmer;
		Controller controller;
		
		if (args[0]=="-h" || args.length<2)
			help();
		
		else {
			controller = new Controller(args[0], Integer.parseInt(args[1]));
			usage();
			String line = null;
			do {
				Scanner scanner = new Scanner(System.in);
			    line = scanner.nextLine();
			    String[] command = line.split(" +");
			    
			    switch (command[0].toUpperCase()) { }
			      
				
			} while(!(line.toUpperCase().equals("QUIT")));
			
		}

	}
	
	private static void help()
	{
		System.out.println("Lancez l'application avec les paramètres [host] [port] pour accéder au service d'échange de Pokeputz");
	}
	
	private static void usage()
	{
		System.out.println("Merci d'avoir choisi notre système d'échange de Pokeputz !");
		System.out.println("Avant de commencer, connectez-vous grâce à la commande : ");
		System.out.println("CONNECT [username] [password]");
		
	}
	
	private static run()
	{
		
	}

}
