package fr.ensibs.pokeputz.trading;

import fr.ensibs.river.River;

public class TraderServer {
	
	River river;
	
	TraderServer(String host, int port)
	{
		this.river = new River(host, port);
		river.run();
	}

}
