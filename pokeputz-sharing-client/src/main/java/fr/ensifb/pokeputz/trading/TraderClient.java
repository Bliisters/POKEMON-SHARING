package fr.ensifb.pokeputz.trading;

import java.rmi.RemoteException;

import fr.ensibs.pokeputz.common.Pokeput;
import fr.ensibs.river.RiverLookup;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.server.ServerTransaction;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

public class TraderClient {
	
	JavaSpace space;
	TransactionManager transactionManager;
	ServerTransaction transaction;
	
	TraderClient(String host, int port)
	{
		System.out.println("Searching for a JavaSpace...");
        RiverLookup finder;
		try {
			finder = new RiverLookup();
			space = (JavaSpace) finder.lookup(host,port, JavaSpace.class);
	        System.out.println("Connection au Javaspace réussie.");
	        
	        transactionManager = (TransactionManager)finder.lookup(host,port, TransactionManager.class);
	        long tID = transactionManager.create(60000).id;
	        transaction = new ServerTransaction(transactionManager,tID); 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Impossible de se connecter au JavaSpace");
		}
	}
	
	
	public void put(String IDAnnonce, Pokeput pokeput)
	{
		TradeForm form = new TradeForm(IDAnnonce, pokeput);
		try {
			space.write(form, null, Long.MAX_VALUE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Pokeput get(String IDAnnonce)
	{
		TradeForm template = new TradeForm(IDAnnonce);
		
		try {
			TradeForm form = (TradeForm) space.takeIfExists(template, null, Long.MAX_VALUE);
			
			if(form!=null)
				return form.getPokeput();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnusableEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
