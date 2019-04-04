package fr.ensibs.pokeputz.authentification;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Authentifier implements IAuthentifier {
	
	public void main() throws NamingException {
		
		String Database = "PokeputzDB";
		String url="jdbc:mysql://localhost:3306/"+Database;
		
		Context context = new InitialContext();
		context.lookup(Database);
		
	}
	
}
