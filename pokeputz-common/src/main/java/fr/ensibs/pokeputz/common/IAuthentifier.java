package fr.ensibs.pokeputz.common;

import java.sql.SQLException;

public interface IAuthentifier {

	public Farmer login(String username, String hashedPass) throws SQLException;
	
}