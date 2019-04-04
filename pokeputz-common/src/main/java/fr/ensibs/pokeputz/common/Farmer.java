package fr.ensibs.pokeputz.common;

import java.util.List;

public class Farmer {
	
	String id;
	String name;
	List<Pokeput> pokeputz;
	
	Farmer(String id, String name, List<Pokeput> pokeputz)
	{
		this.id=id;
		this.name=name;
		this.pokeputz=pokeputz;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Pokeput> getPokeputz() {
		return pokeputz;
	}
	
	public void addPokeput(Pokeput p)
	{
		pokeputz.add(p);
	}
	
	

}
