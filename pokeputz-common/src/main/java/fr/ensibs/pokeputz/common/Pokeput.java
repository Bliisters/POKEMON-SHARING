package fr.ensibs.pokeputz.common;

import net.jini.core.entry.Entry;

public class Pokeput implements Entry {
	
	public String id;
	public String name;
	public Integer level;
	
	Pokeput(String id, String name, Integer level)
	{
		this.id=id;
		this.name=name;
		this.level=level;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getLevel() {
		return level;
	}

}
