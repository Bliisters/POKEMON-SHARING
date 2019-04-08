package fr.ensibs.pokeputz.authentification;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.ensibs.pokeputz.common.Farmer;
import fr.ensibs.pokeputz.util.DatabaseCRUD;

public class Authentifier extends RemoteObject implements IAuthentifier {
	
	private static final long serialVersionUID = -6308709622514130428L;
	
	private Connection conn;
	
	private DatabaseCRUD CRUD;
	
	public Authentifier (DatabaseCRUD CRUD) {
		this.CRUD = CRUD;
	}
	
	public void LoadDriver(String user, String pass) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
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
	
	
	public static void main(String[] args) throws NumberFormatException, RemoteException, AlreadyBoundException {

		String user = (args.length < 1) ? null : args[0];
		String pass = (args.length < 2) ? null : args[1];
		
		//Server MailBox = new Server();
		
		Registry registry = LocateRegistry.createRegistry(8985);
		
		Authentifier A = new Authentifier(new DatabaseCRUD("test", "test"));
		registry.bind("Authentifier", A );
        
	}

	@Override
	public Farmer login(String username, String hashedPass) throws SQLException {
		
		String SQLSentence = "SELECT FarmerSalt FROM Salting WHERE FarmerToken = ?";
		
		PreparedStatement statement = conn.prepareStatement("SELECT FarmerToken FROM Farmers WHERE FarmerName = ? ");
		statement.setString(0, username);
		String Token = statement.executeQuery().getString(0);
		
		statement = conn.prepareStatement("SELECT FarmerSalt FROM Salting WHERE FarmerToken = ? ");
		statement.setString(0, Token);
		String salt = statement.executeQuery().getString(0);
		String pass = hashedPass + salt;
		
		statement = conn.prepareStatement("SELECT FarmerToken FROM Farmers WHERE FarmerName = ? AND FarmerPass = ?");
		statement.setString(0, username);
		statement.setString(1, pass);
		String DefinitiveToken = statement.executeQuery().getString(0);
		
		Farmer result = CRUD.getFarmer(DefinitiveToken);
				
		return result;
	}
	
}
