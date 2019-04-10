package fr.ensibs.pokeputz.publisher;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.BytesMessage;

import fr.ensibs.pokeputz.common.DefaultAdvertisement;
import fr.ensibs.pokeputz.interfaces.Advertisement;

public class MessengingUser implements User {
	/**
	  * object used to communicate through openJSM
	  */
	  private AdvertisementSharingMessenger messenger;

	  String name;
	  
	  /**
	  * Constructor
	  *
	  * @param name the user name in the file sharing community
	  * @param directory the user directory where his photos are stored
	  * @param messenger object used to communicate through openJSM
	  */
	  public MessengingUser(String name, AdvertisementSharingMessenger messenger) throws Exception
	  {
	    this.name=name;
	    this.messenger = messenger;
	    this.messenger.setUser(this);
	  }
	  
	  @Override
	  public String getName() {
		  return this.name;
	  }

	  @Override
	  public boolean share(Advertisement advertisement)
	  {
	    try {
	      this.messenger.send(advertisement);
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return false;
	  }

	  @Override
	  public boolean setFilter(Properties tags)
	  {
	    try {
	      this.messenger.subscribe(tags);
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return false;
	  }

	  /**
	  * Method invoked when a message has been received through JMS
	  *
	  * @param message the JMS received message
	  */
	  public void onMessageReceived(BytesMessage message)
	  {
	    try {
	      // initialize tags
	      Properties tags = new Properties();
	      for (Enumeration e = message.getPropertyNames(); e.hasMoreElements();) {
	        String name = (String) e.nextElement();
	        if (!name.equals("owner") && !name.equals("message") && ! name.startsWith("JMS")) {
	          tags.put(name, message.getStringProperty(name));
	        }
	      }

	      // write the message body to a binary file
	      String msg = message.getStringProperty("message");
	      String owner = message.getStringProperty("owner");
	      byte[] data = new byte[(int)message.getBodyLength()];
	      message.readBytes(data);


	      // create a photo and add it to the received photos
	      DefaultAdvertisement advertisement = new DefaultAdvertisement(msg, tags, owner);
	      this.receive(advertisement);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	@Override
	public void receive(Advertisement advertisement) {
		System.out.println("Photo received: " + advertisement);
		
	}
}
