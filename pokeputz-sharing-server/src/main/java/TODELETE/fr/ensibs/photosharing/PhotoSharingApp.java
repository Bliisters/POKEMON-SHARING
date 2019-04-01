package TODELETE.fr.ensibs.photosharing;

import java.util.Properties;
import java.util.Scanner;

import fr.ensibs.joram.Joram;
import fr.ensibs.joram.JoramAdmin;

import java.io.File;
import java.io.IOException;

/**
* The entry point for the user photo sharing application that allows to enter
* descriptions and tags of photos to be shared and filter and choose photos
* from their tags
*/
public class PhotoSharingApp
{

  /**
  * The user of the application
  */

  /**
  * Print a usage message and exit
  */
  private static void usage()
  {
    System.out.println("Usage: java -jar photosharing-server-v1.0 <port>");
    System.out.println("Launch the server photo sharing application");
    System.out.println("with:");
    System.out.println("<port> the port where the server is started");
    System.exit(0);
  }

  /**
  * Application entry point
  *
  * @param args see usage
  */
  public static void main(String[] args)
  {
	  if (args.length < 1)
	  {
		  usage();
	  }
	  else
	  {
		  int port = Integer.parseInt(args[0]);
		  
		  try {
			Joram server = new Joram(port);

			server.run();
			

			JoramAdmin admin = new JoramAdmin("localhost", port);
			admin.createTopic("PhotosharingTopic");
			
		  } catch (Exception e) {
				System.err.println("Unable to start the server: ");
				e.printStackTrace();
		  }
		  
	  }
    
  }

}