package fr.ensibs.pokeputz.publisher;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;


public class AdvertisementSharingListener implements MessageListener
{

	  /**
	  * the instance that processes BytesMessage messages
	  */
	  private MessengingUser user;

	  /**
	  * Constructor
	  *
	  * @param user instance that processes BytesMessage messages
	  */
	  public AdvertisementSharingListener(MessengingUser user)
	  {
	    this.user = user;
	  }

	  @Override
	  public void onMessage(Message message) {
	    if (message instanceof BytesMessage) {
	      this.user.onMessageReceived((BytesMessage)message);
	    }
	  }
}
