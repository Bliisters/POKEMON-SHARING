package fr.ensibs.pokeputz.authentification;

import java.sql.SQLException;

import fr.ensibs.pokeputz.common.Farmer;

public interface IAuthentifier {

	public Farmer login(String username, String hashedPass) throws SQLException;
	
}