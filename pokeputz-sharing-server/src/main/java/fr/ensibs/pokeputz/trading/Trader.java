package fr.ensibs.pokeputz.trading;

import fr.ensibs.river.River;

public class Trader {
	
	River river;
	
	Trader(String host, int port)
	{
		this.river = new River(host, port);
		river.run();
	}

}
