package fr.ensibs.pokeputz.publisher;

import java.rmi.server.RemoteObject;
import java.sql.SQLException;
import java.util.Vector;

import fr.ensibs.pokeputz.common.PokePost;
import fr.ensibs.pokeputz.interfaces.IPublisher;
import fr.ensibs.pokeputz.util.DatabaseCRUD;

public class Publisher extends RemoteObject implements IPublisher {
	
	private static final long serialVersionUID = -1978841931618020820L;
	
	private DatabaseCRUD CRUD;
	
	private AdvertisementSharingServer JMSPublisher;
	
	public Publisher (DatabaseCRUD CRUD, AdvertisementSharingServer JMSPublisher) {
		this.CRUD = CRUD;
		this.JMSPublisher = JMSPublisher;
	}
	
	@Override
	public void publish(PokePost post) throws SQLException {
		CRUD.postPokePost(post);
	}
	
	@Override
	public Vector<PokePost> consultPosts() throws SQLException {
		return CRUD.getAllPokePosts();
	}
	
	@Override
	public void answerPost(String PostToken) throws SQLException {
		PokePost p = CRUD.getPokePost(PostToken);
		p.setAnswered();
		
		//TODO: JMS LINK - CREATE NOTIFICATION
	}
	
}
