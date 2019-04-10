package fr.ensibs.pokeputz.interfaces;

import java.sql.SQLException;
import java.util.Vector;

import fr.ensibs.pokeputz.common.PokePost;

public interface IDatabasePublisher {

	void publish(PokePost post) throws SQLException;

	Vector<PokePost> consultPosts() throws SQLException;

	void answerPost(String PostToken) throws SQLException;

}
