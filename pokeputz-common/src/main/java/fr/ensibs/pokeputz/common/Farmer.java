package fr.ensibs.pokeputz.common;

import java.util.Vector;

public class Farmer {

	private String token;
	private String name;
	private Vector<Pokeput> pokeputz;

	Farmer(String token, String name, Vector<Pokeput> pokeputz)
	{
		this.token=token;
		this.name=name;
		this.pokeputz=pokeputz;
	}

	public Farmer() {
		this.token = new String();
		this.name = new String();
		this.pokeputz = new Vector<Pokeput>();
	}

	public String getToken() {
		return token;
	}

	public String getName() {
		return name;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<Pokeput> getPokeputz() {
		return pokeputz;
	}

	public void addPokeput(Pokeput p)
	{
		pokeputz.add(p);
	}

	public void delPokeput(Pokeput p)
	{
		pokeputz.remove(p);
	}
}
