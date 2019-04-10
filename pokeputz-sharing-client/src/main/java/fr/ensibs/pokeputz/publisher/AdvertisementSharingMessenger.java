package fr.ensibs.pokeputz.publisher;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import fr.ensibs.pokeputz.interfaces.Advertisement;

public class AdvertisementSharingMessenger {
	/**
	  * the name of the topic used to exchange photos
	  */
	  private static final String TOPIC_NAME = "TRADES";

	  /**
	  * the JMS connection
	  */
	  private Connection connection;

	  /**
	  * the JMS session, used to create and send messages
	  */
	  private Session senderSession;

	  /**
	  * the topic used to exchange photos
	  */
	  private Destination destination;

	  /**
	  * the message producer used to send photos to the shared topic
	  */
	  private MessageProducer sender;

	  /**
	  * a message receiver initialized when the {@link #subscribe} method is called
	  */
	  private MessageConsumer receiver;

	  /**
	  * the listener invoked when a message is received
	  */
	  private MessageListener listener;

	  /**
	  * the current user
	  */
	  private MessengingUser user;

	  /**
	  * Constructor: initialize the JMS connection, session and message producer, and
	  * start the connection
	  *
	  * @param host the JMS server host name
	  * @param port the JMS server port number
	  */
	  public AdvertisementSharingMessenger(String host, int port) throws Exception
	  {
	    // initialize the JMS context properties
	    System.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
	    System.setProperty("java.naming.factory.host", host);
	    System.setProperty("java.naming.factory.port", Integer.toString(port));
	    Context context = new InitialContext();

	    // create the JMS connection and session
	    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
	    this.connection = factory.createConnection();
	    this.senderSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	    // create a message producer
	    this.destination = (Destination) context.lookup(TOPIC_NAME);
	    this.sender = this.senderSession.createProducer(destination);

	    // start the JMS connection
	    this.connection.start();
	  }

	  /**
	  * Set the current user
	  *
	  * @param user the user
	  */
	  public void setUser(MessengingUser user) throws Exception
	  {
	    this.user = user;
	    this.listener = new AdvertisementSharingListener(user);
	  }

	  /**
	  * Send a JMS message to the shared topic that contains a photo binary data and
	  * has properties that represent the photo tags
	  *
	  * @param photo the photo to be sent
	  */
	  public void send(Advertisement advertisement) throws Exception
	  {
		  ByteArrayOutputStream b = new ByteArrayOutputStream();
		  ObjectOutputStream in = new ObjectOutputStream(b);
		  in.writeObject(advertisement);
		  byte[] data =  b.toByteArray();
		  
	    BytesMessage message = this.senderSession.createBytesMessage();
	      message.writeBytes(data);

	    message.setStringProperty("owner", advertisement.getOwner());
	    message.setStringProperty("message", advertisement.getMessage());
	    Properties tags = advertisement.getTags();
	    for (String key : tags.stringPropertyNames()) {
	      message.setStringProperty(key, tags.getProperty(key));
	    }
	    this.sender.send(message);
	  }

	  /**
	  * Create a receiver that subscribes to the shared topic to receive messages
	  * having the given tags as properties
	  *
	  * @param tags the tags that interest the receiver
	  */
	  public void subscribe(Properties tags) throws Exception
	  {
	    if (this.receiver != null) {
	      this.receiver.close();
	    }
	    Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    String selector = makeSelector(tags);
	    if (selector != null) {
	      this.receiver = consumerSession.createConsumer(destination, selector);
	    } else {
	      this.receiver = consumerSession.createConsumer(destination);
	    }
	    this.receiver.setMessageListener(this.listener);
	  }

	  /*
	  * Close the current JMS connection
	  */
	  public void close()
	  {
	    try {
	      this.connection.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  /**
	  * Create a selector string from the given tags
	  *
	  * @param tags properties used to compose the selector
	  */
	  private String makeSelector(Properties tags)
	  {
	    Iterator<String> it = tags.stringPropertyNames().iterator();
	    if (it.hasNext()) {
	      String key = it.next();
	      String selector = key + " = '" + tags.getProperty(key) + "'";
	      while (it.hasNext()) {
	        key = it.next();
	        selector += " OR " + key + " = '" + tags.getProperty(key) + "'";
	      }
	      return selector;
	    }
	    return null;
	  }
}
