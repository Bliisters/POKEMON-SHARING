package fr.ensibs.pokeputz.trading;

import fr.ensibs.river.RiverLookup;
import net.jini.space.JavaSpace;

public class MainTradeServer {

	RiverLookup finder;
	
	public static void main(String[] args) {
		TraderServer ts = new TraderServer("localhost", 7491);
		RiverLookup finder = null;
		try {
			finder = new RiverLookup(); // Create a lookup for the javaspace.
		} catch (Exception e) {
			System.err.println("Unable to create a RiverLookup: ");
			e.printStackTrace();
			System.exit(1);
		}
        try {
			JavaSpace space = (JavaSpace) finder.lookup("host", 0, JavaSpace.class); // And look for a javaspace on the server coordinates
		} catch (Exception e) {
			System.err.println("Unable to find the Javaspace: Is the server running? Check host and port.");
			e.printStackTrace();
			System.exit(1);
		}
	}

}
