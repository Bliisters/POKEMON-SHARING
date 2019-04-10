package fr.ensibs.pokeputz.common;

import java.util.Properties;

import fr.ensibs.pokeputz.interfaces.Advertisement;

/**
 * An advertisement shared by a user composed of a message and tags that describe the advertisement
 */
public class DefaultAdvertisement implements Advertisement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7581319554410761312L;

	/**
	 * the advertisement tags that describe the advertisement
	 */
	private Properties tags;

	/**
	 * the string file that contains the message
	 */
	private String message;

	/**
	 * the user that shared the photo, considered as the photo owner
	 */
	private String owner;

	/**
	 * Constructor
	 *
	 * @param message the string message
	 * @param tags the advertisement tags
	 * @param owner the user that shared the advertisement
	 */
	public DefaultAdvertisement(String message, Properties tags, String owner)
	{
		this.message = message;
		this.tags = tags;
		this.owner = owner;
	}

	
	public Properties getTags()
	{
		return this.tags;
	}

	
	public String getMessage()
	{
		return this.message;
	}

	
	public String getOwner()
	{
		return this.owner;
	}

	
	public String toString()
	{
		return "{ Advertissement: message=" + message + ",owner=" + owner + ",tags=" + tags + " }";
	}

}
