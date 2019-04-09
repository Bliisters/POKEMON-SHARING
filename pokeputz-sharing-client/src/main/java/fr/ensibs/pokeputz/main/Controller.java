package fr.ensibs.pokeputz.main;

import fr.ensifb.pokeputz.trading.TraderClient;

public class Controller {
	
	TraderClient trader;
	
	public Controller(String host, int port)
	{
		this.trader = new TraderClient(host, port);
		
	}

}
