package fr.ensibs.pokeputz.common;


import net.jini.core.entry.Entry;

public class Pokeput implements Entry {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4575757152137871905L;
	
	public static enum TYPE {NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DARK, DRAGON, STEEL, FAIRY};
	
	private String token;
	private String name;
	private TYPE types[];
	//private Integer level;
	
	Pokeput(String token, String name, TYPE type1, TYPE type2)
	{
		this.token = token;
		this.name = name;
		this.types = new TYPE[2];
		types[0] = type1;
		types[1] = type2;
	}

	public Pokeput() {
		this.token = new String();
		this.name = new String();
		this.types = new TYPE[2];
	}

	public String getToken() {
		return token;
	}

	public String getName() {
		return name;
	}
	
	public TYPE[] getTypes() {
		return types;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public void setName(String token) {
		this.token = token;
	}
	
	public void setTypes(TYPE[] types) {
		this.types = types;
	}
	
	public void setTypes(TYPE type1, TYPE type2) {
		this.types[0] = type1;
		this.types[1] = type2;
	}

}
