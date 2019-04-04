package fr.ensibs.pokeputz.common;

import java.rmi.Remote;
import java.util.Properties;

public interface JMSControllerInterface extends Remote {

	public void share(String message,  Properties tags);

	public void filter(Properties tags);
	
	public void quit();

}
