package fr.ensibs.pokeputz.publisher;

import fr.ensibs.joram.Joram;
import fr.ensibs.joram.JoramAdmin;

/**
 * Program that launches a JMS server and creates a topic for the advertisement sharing
 * application
 */

public class AdvertisementSharingServer {

	/**
	 * the topic name
	 */
	private static final String TOPIC_NAME = "TRADES";

	/**
	 * the JMS administration instance
	 */
	private JoramAdmin admin;

	/**
	 * Print a usage message and exits
	 */
	private static void usage()
	{
		System.out.println("java PhotoSharingServer <port>");
		System.out.println("Launch a JMS server and creates the " + TOPIC_NAME + " topic if needed");
		System.exit(0);
	}

	/**
	 * Program entry point
	 *
	 * @param args see {@link usage()}
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length != 1 || args[0].equals("-h")) {
			usage();
		}

		try {
			int port = Integer.parseInt(args[0]);
			AdvertisementSharingServer server = new AdvertisementSharingServer(port);
			server.makeTopic(TOPIC_NAME);
		} catch (NumberFormatException e) {
			usage();
		}
	}

	/**
	 * Constructor. Launch the JMS server and create the admin instance
	 *
	 * @param port the openjms port number
	 */
	public AdvertisementSharingServer(int port) throws Exception
	{
		Joram joram = new Joram(port);
		joram.start();
		Thread.sleep(3000);
		this.admin = new JoramAdmin("localhost", port);
	}

	/**
	 * Create a topic having the given name if it doesn't already exists
	 *
	 * @param topicName name of the topic to be created
	 */
	public void makeTopic(String topicName) throws Exception
	{
		if (!this.hasTopic(topicName)) {
			this.admin.createTopic(topicName);
		} else {
			System.out.println(topicName + " topic already exists");
		}
	}

	/**
	 * Check whether a topic having the given name exists
	 *
	 * @param topicName name of the topic
	 */
	private boolean hasTopic(String topicName) throws Exception
	{
		for (String name : this.admin.getTopics()) {
			if (name.equals(topicName)) {
				return true;
			}
		}
		return false;
	}


}
