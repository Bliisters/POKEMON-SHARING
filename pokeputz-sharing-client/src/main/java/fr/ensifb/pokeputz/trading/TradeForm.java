package fr.ensifb.pokeputz.trading;

import fr.ensibs.pokeputz.common.Pokeput;
import net.jini.core.entry.Entry;

public class TradeForm implements Entry{
	
	public String IDAnnonce; 
	public Pokeput pokeput;
	
	TradeForm(String IDAnnonce, Pokeput pokeput)
	{
		this.IDAnnonce=IDAnnonce;
		this.pokeput=pokeput;
	}
	
	TradeForm(String IDAnnonce)
	{
		this.IDAnnonce=IDAnnonce;
	}
	
	public String getIDAnnonce()
	{
		return this.IDAnnonce;
	}
	
	public Pokeput getPokeput()
	{
		return this.pokeput;
	}

}
