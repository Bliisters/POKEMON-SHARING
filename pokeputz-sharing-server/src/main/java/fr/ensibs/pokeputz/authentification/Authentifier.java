package fr.ensibs.pokeputz.authentification;

import java.rmi.server.RemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

import fr.ensibs.pokeputz.common.Farmer;
import fr.ensibs.pokeputz.interfaces.IAuthentifier;
import fr.ensibs.pokeputz.util.DatabaseCRUD;

public class Authentifier extends RemoteObject implements IAuthentifier {
	
	private static final long serialVersionUID = -6308709622514130428L;
	
	private Connection conn;
	
	private DatabaseCRUD CRUD;
	
	private static final Random random = new Random();
	private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
	
	public Authentifier (String user, String pass, DatabaseCRUD CRUD) {
		this.CRUD = CRUD;
		this.LoadDriver(user, pass);
	}
	
	public void LoadDriver(String user, String pass) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	ex.printStackTrace();
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
			

	@Override
	public Farmer login(String username, String hashedPass) throws SQLException {
		
		String SQLSentence = "SELECT FarmerSalt FROM Salting WHERE FarmerToken = ?";
		
		PreparedStatement statement = conn.prepareStatement("SELECT FarmerToken FROM Farmers WHERE FarmerName = ? ");
		statement.setString(1, username);
		ResultSet resultset = statement.executeQuery();
		resultset.first();
		String Token = resultset.getString(1);
		
		statement = conn.prepareStatement("SELECT FarmerSalt FROM Salting WHERE FarmerToken = ? ");
		statement.setString(1, Token);
		resultset = statement.executeQuery();
		resultset.first();
		String salt = resultset.getString(1);
		String pass = hashedPass + salt;
		
		statement = conn.prepareStatement("SELECT FarmerToken FROM Farmers WHERE FarmerName = ? AND FarmerPass = ?");
		statement.setString(1, username);
		statement.setString(2, pass);
		resultset = statement.executeQuery();
		resultset.first();
		String DefinitiveToken = resultset.getString(1);
		
		Farmer result = CRUD.getFarmer(DefinitiveToken);
		
		return result;
	}
	
	public void signIn(String username, String clearpass) throws SQLException {
				
		PreparedStatement statement = conn.prepareStatement("SELECT FarmerToken FROM Farmers WHERE FarmerName = ? ");
		statement.setString(1, username);
		ResultSet resultset = statement.executeQuery();
		resultset.first();
		try
		{
			resultset.getString(1);
			System.out.println("User '"+username+"' already exists.");
			return;
		} catch (Exception E) {
			
		}
		
		statement = conn.prepareStatement("INSERT INTO Farmers VALUES ( ? , ? , ? )");
		String Token = nextToken(10);
		statement.setString(1, Token);
		statement.setString(2, username);
		String hashedpass = DigestUtils.sha256Hex(clearpass);
		String salt = nextToken(5);
		hashedpass = hashedpass + salt;
		statement.setString(3, hashedpass);
		statement.executeUpdate();
		
		statement = conn.prepareStatement("INSERT INTO Salting VALUES ( ? , ? )");
		statement.setString(1, Token);
		statement.setString(2, salt);
		statement.executeUpdate();
		
	}

	private static String nextToken(int length) {
	    StringBuilder token = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	        token.append(CHARS.charAt(random.nextInt(CHARS.length())));
	    }
	    return token.toString();
	}
}
