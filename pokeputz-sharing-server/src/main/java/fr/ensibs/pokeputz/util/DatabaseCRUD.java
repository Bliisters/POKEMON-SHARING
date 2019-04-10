package fr.ensibs.pokeputz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.codec.digest.DigestUtils;

import fr.ensibs.pokeputz.common.Farmer;
import fr.ensibs.pokeputz.common.PokePost;
import fr.ensibs.pokeputz.common.Pokeput;


public class DatabaseCRUD {

	Connection conn;
	
	public DatabaseCRUD(String user, String pass) {
		LoadDriver(user, pass);
	}
	
	public void LoadDriver(String user, String pass) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	
        	System.err.println("Driver class not found");
            // handle the error
        }
        
        try {
            conn =
               DriverManager.getConnection("jdbc:mysql://localhost/PokeputzDB?" +
                                           "user="+user+"&password="+pass+"&serverTimezone=UTC");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        
	}
	
	public Farmer getFarmer(String Token) throws SQLException {
		// TODO Auto-generated method stub
		Farmer result = new Farmer();
		
		PreparedStatement statement = conn.prepareStatement("SELECT FarmerToken, FarmerName FROM Farmers WHERE FarmerToken = ? ");
		statement.setString(1, Token);
		ResultSet resultset = statement.executeQuery();
		resultset.first();
		result.setToken(resultset.getString(1));
		result.setName(resultset.getString(2));
		
		statement = conn.prepareStatement("SELECT PokeToken FROM PossessTable WHERE FarmerToken = ? ");
		statement.setString(1, Token);
		resultset = statement.executeQuery();
		resultset.beforeFirst();
		String Poketoken = "";
		while (resultset.next())
		{
			Poketoken = resultset.getString(1);
			result.addPokeput(this.getPokeput(Poketoken));
		}
		
		return result;
	}
	
	public Pokeput getPokeput(String Token) throws SQLException {
		// TODO Auto-generated method stub
		Pokeput result = new Pokeput();
		
		PreparedStatement statement = conn.prepareStatement("SELECT PokeToken, PokeName, PokeType1, PokeType2 FROM Pokeputz WHERE PokeToken = ? ");
		statement.setString(1, Token);
		ResultSet resultSet = statement.executeQuery();
		resultSet.first();
		result.setToken(resultSet.getString(1));
		result.setName(resultSet.getString(2));
		result.setTypes( Enum.valueOf(Pokeput.TYPE.class, resultSet.getString(3)) , Enum.valueOf(Pokeput.TYPE.class, resultSet.getString(4)) );
		
		return result;
	}

	public void postPokePost(PokePost post) throws SQLException {
		
		PreparedStatement statement = conn.prepareStatement("INSERT INTO PokePosts VALUES ( ? , ? , ? , ? , ? )");
		statement.setString(1, post.getPostToken());
		statement.setString(2, post.getAuthor().getToken());
		statement.setString(3, post.getPokeGiven().getToken());
		statement.setString(4, post.getPokeSearched().getToken());
		statement.setBoolean(5, post.getAnswered());
		statement.executeUpdate();
		
	}

	public Vector<PokePost> getAllPokePosts() throws SQLException {
		
		Vector<PokePost> result = new Vector<PokePost>();
		
		PreparedStatement statement = conn.prepareStatement("SELECT PostToken, PostAthor, PostPokeGiven, PostPokeLookedFor FROM PokePosts WHERE Answered = FALSE ");
		ResultSet resultSet = statement.executeQuery();
		resultSet.beforeFirst();
		PokePost p = new PokePost();
		while (resultSet.next())
		{
			p.setPostToken(resultSet.getString(1));
			p.setAuthor(this.getFarmer(resultSet.getString(2)));
			p.setPokeGiven(this.getPokeput(resultSet.getString(3)));
			p.setPokeSearched(this.getPokeput(resultSet.getString(4)));
			result.add(p);
		}
		return result;
	}
	
	public PokePost getPokePost(String token) throws SQLException {
		
		PokePost result = new PokePost();
		
		PreparedStatement statement = conn.prepareStatement("SELECT PostToken, PostAthor, PostPokeGiven, PostPokeLookedFor, Answered FROM PokePosts WHERE PostToken = ? ");
		statement.setString(1, token);
		ResultSet resultSet = statement.executeQuery();
		resultSet.beforeFirst();
		if (resultSet.next())
		{
			PokePost p = new PokePost();
			p.setPostToken(resultSet.getString(1));
			p.setAuthor(this.getFarmer(resultSet.getString(2)));
			p.setPokeGiven(this.getPokeput(resultSet.getString(3)));
			p.setPokeSearched(this.getPokeput(resultSet.getString(4)));
			if (resultSet.getBoolean(5))
				p.setAnswered();
					
			result = p;
		}
		else
		{
			result = null;
		}
		return result;
	}
	
	public void answerPokePost(String token) throws SQLException {
		
		PreparedStatement statement = conn.prepareStatement("UPDATE PokePosts\r\n" + 
				"SET Answered = TRUE " + 
				"WHERE PostToken = ? ;");
		statement.setString(1, token);
		statement.executeUpdate();
	}

}
