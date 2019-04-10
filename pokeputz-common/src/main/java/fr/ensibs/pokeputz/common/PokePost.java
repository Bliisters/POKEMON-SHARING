package fr.ensibs.pokeputz.common;

public class PokePost {

	private String postToken;
	private Farmer author;
	private Pokeput pokeGiven;
	private Pokeput pokeSearched;
	private Boolean answered;
	
	public PokePost()
	{
		this.postToken = null;
		this.author = null;
		this.pokeGiven = null;
		this.pokeSearched = null;
		this.answered = false;
	}
	
	public String getPostToken() {
		return postToken;
	}
	public void setPostToken(String postToken) {
		this.postToken = postToken;
	}
	
	public Farmer getAuthor() {
		return author;
	}
	public void setAuthor(Farmer author) {
		this.author = author;
	}
	public Pokeput getPokeGiven() {
		return pokeGiven;
	}
	public void setPokeGiven(Pokeput pokeGiven) {
		this.pokeGiven = pokeGiven;
	}
	public Pokeput getPokeSearched() {
		return pokeSearched;
	}
	public void setPokeSearched(Pokeput pokeSearched) {
		this.pokeSearched = pokeSearched;
	}
	
	public Boolean getAnswered() {
		return answered;
	}
	public void setAnswered() {
		this.answered = true;
	}
	
	public boolean equals(PokePost p)
	{
		return (this.getPostToken() == p.getPostToken());
	}

	
}
