package fr.ensibs.pokeputz.publisher;

import java.util.Properties;
import java.io.Serializable;

/**
* An advertisement shared by a user composed of a string message and tags that describe the advertisement
*/
public interface Advertisement extends Serializable
{

  /**
  * Give the advertisement tags that describe the advertisement in the form key=value
  *
  * @return the advertisement tags
  */
  Properties getTags();

  /**
  * Give the string that contains the message
  *
  * @return the photo file
  */
  String getMessage();

  /**
  * Give the user that shared the advertisement, considered as the advertisement owner
  *
  * @return the advertisement owner
  */
  String getOwner();
}
