package fr.ensibs.pokeputz.publisher;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;

public class AdvertisementSharingManager {
	
	/**
	  * The user of the application
	  */
	  private final User user;

	  /**
	  * object used to communicate through openJSM
	  */
	  private AdvertisementSharingMessenger messenger;

	  /**
	  * Constructor
	  *
	  * @param userName the user name in the community
	  * @param directory the local directory where photos are stored
	  * @param host the server host name
	  * @param port the server port number
	  */
	  public AdvertisementSharingManager(String userName, String host, int port) throws Exception
	  {
	    this.messenger = new AdvertisementSharingMessenger(host, port);
	    this.user = new MessengingUser(userName, messenger);
	  }

	  /**
	  * Launch the application process that executes user commands: SHARE, FILTER
	  */
	  /*public void run()
	  {
	    System.out.println("Hello, " + this.user.getName() + ". Enter commands:"
	    + "\n QUIT                     to quit the application"
	    + "\n SHARE <filename> <tags>  to share a new photo"
	    + "\n FILTER <tags>            to specify the photos you are interested in"
	    + "\n where <tags> is a list of tags in the form \"key1=value1 key2=value2\"");

	    Scanner scanner = new Scanner(System.in);
	    String line = scanner.nextLine();
	    while (!line.equals("quit") && !line.equals("QUIT")) {
	      String[] command = line.split(" +");
	      switch (command[0]) {
	        case "share":
	        case "SHARE":
	        if (command.length >= 2) {
	          String message = command[1];
	          Properties tags = parseTags(command, 2);
	          share(message, tags);
	        } else {
	          System.err.println("Usage: share <filename> <tags>");
	        }
	        break;
	        case "filter":
	        case "FILTER":
	        filter(parseTags(command, 1));
	        break;
	        default:
	        System.err.println("Unknown command: \"" + command[0] + "\"");
	      }
	      line = scanner.nextLine();
	    }
	    quit();
	  }
	  */

	  /**
	  * Share a new photo
	  *
	  * @param file the photo file in the local directory
	  * @param tags a list of tags that describe the photo
	  */
	  public void share(String message, Properties tags)
	  {
	      Advertisement advertisement = new DefaultAdvertisement(message, tags, this.user.getName());
	      boolean success = this.user.share(advertisement);
	      if (success) {
	        System.out.println(advertisement + " has been shared");
	      }
	  }

	  /**
	  * Specify the photos the user is interested in by setting new tags
	  *
	  * @param tags the new user tags
	  */
	  public void filter(Properties tags)
	  {
	    boolean success = this.user.setFilter(tags);
	    if (success) {
	      System.out.println("Filter " + tags + " has been set");
	    }
	  }

	  /**
	  * Stop the application
	  */
	  public void quit()
	  {
	    this.messenger.close();
	    System.exit(0);
	  }

	  /**
	  * Transform a list of words in the form key=value to tags
	  *
	  * @param tokens a list of strings
	  * @param startIdx the index of the first token in the given list that represent
	  * a tag
	  * @return the tags
	  */
	  public Properties parseTags(String[] tokens, int startIdx)
	  {
	    Properties tags = new Properties();
	    for (int i=startIdx; i< tokens.length; i++) {
	      String[] token = tokens[i].split("=");
	      if (token.length == 2) {
	        tags.put(token[0], token[1]);
	      } else {
	        System.err.println("Cannot parse tag: \"" + tokens[i] + "\"");
	      }
	    }
	    return tags;
	  }

}
