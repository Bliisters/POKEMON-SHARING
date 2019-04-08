package fr.ensibs.pokeputz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensibs.pokeputz.common.Farmer;
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

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
        
        try {
            conn =
               DriverManager.getConnection("jdbc:mysql://localhost/PokeputzDB?" +
                                           "user="+user+"&password="+pass);
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
		statement.setString(0, Token);
		ResultSet resultset = statement.executeQuery();
		result.setToken(resultset.getString(0));
		result.setName(resultset.getString(1));
		
		statement = conn.prepareStatement("SELECT PokeToken FROM PossessTable WHERE FarmerToken = ? ");
		statement.setString(0, Token);
		resultset = statement.executeQuery();
		
		String Poketoken = resultset.getString(0);
		while (Poketoken != null)
		{
			result.addPokeput(this.getPokeput(Poketoken));
		}
		
		return result;
	}
	
	public Pokeput getPokeput(String Token) throws SQLException {
		// TODO Auto-generated method stub
		Pokeput result = new Pokeput();
		
		PreparedStatement statement = conn.prepareStatement("SELECT PokeToken, PokeName, PokeType1, PokeType2 FROM Pokeputz WHERE PokeToken = ? ");
		statement.setString(0, Token);
		ResultSet resultSet = statement.executeQuery();
		result.setToken(resultSet.getString(0));
		result.setName(resultSet.getString(1));
		result.setTypes( Enum.valueOf(Pokeput.TYPE.class, resultSet.getString(2)) , Enum.valueOf(Pokeput.TYPE.class, resultSet.getString(3)) );
		
		return result;
	}

}
