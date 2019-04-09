package fr.ensibs.pokeputz.main;

import fr.ensibs.pokeputz.common.Farmer;

public class Application {

	Farmer farmer;
	Controller controller;
	
	public static void main(String[] args) {
		
		if (args[0]=="-h" || args.length<2)
			help();
		
		if(controllerConnect(args[0], Integer.parseInt(args[1])));
		
		


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
	
	private static boolean controllerConnect(String host, int port)
	{
		this.controller = new Controller(host, port);
	}

}
