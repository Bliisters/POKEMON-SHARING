package fr.ensibs.pokeputz.publisher;

import java.util.Properties;

import fr.ensibs.pokeputz.interfaces.Advertisement;

public interface User {
	
	  /**
	  * Give the user name
	  *
	  * @return the user name
	  */
	  String getName();

	  /**
	  * Share a new photo
	  *
	  * @param photo the new photo to be shared
	  * @return true if the photo has been successfully shared
	  */
	  boolean share(Advertisement advertisement);

	  /**
	  * Receive a new photo
	  *
	  * @param photo the new photo that has been received
	  */
	  void receive(Advertisement advertisement);

	  /**
	  * Set new tags that specify the photos the user is interested in
	  *
	  * @param tags the new tags
	  * @return true if the filter has been successfully set
	  */
	  boolean setFilter(Properties tags);

}
