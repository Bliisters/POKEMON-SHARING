package fr.ensibs.pokeputz.authentification;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import fr.ensibs.pokeputz.common.Farmer;
import fr.ensibs.pokeputz.interfaces.IAuthentifier;

public class AuthentifierClient {
	
	  /**
	  * the name of the remote object exported in the name service
	  */
	  private static final String REMOTE_AUTHENTIFIER = "AUTHENTIFIER";
	  
	  /**
	   * the authentifier
	   */
	   private IAuthentifier authentifier;
	   
	   public AuthentifierClient(String host, int port) throws RemoteException, NotBoundException {
		   Registry registry = LocateRegistry.getRegistry(host, port);
		   IAuthentifier authentifier = (IAuthentifier) registry.lookup(REMOTE_AUTHENTIFIER);
		   this.authentifier=authentifier;
	   }
	   
	   public Farmer login(String username, String hashedPass) throws SQLException {
		   return this.authentifier.login(username, hashedPass);
	   }
	   
	   

}
