package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private static Connection con = null;
	
	public static void creaConnessione() throws SQLException {		
			String url = "jdbc:sqlite:test.db";
            con = DriverManager.getConnection(url);
            creaTabella();
	}
	
	public static void chiudiConnessione() throws SQLException {
		if(con == null)
			return;		
		con.close();
		con = null;
	}
	
	private static void creaTabella() throws SQLException {
		if(con == null || con.isClosed())
			return;
		String query = "CREATE TABLE IF NOT EXISTS utenti(username varchar(50), password varchar(50));";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
	
	public static void inserisciDati(String username, String password) throws SQLException {
		if(con == null || con.isClosed())
			return;		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO utenti VALUES('"+username+"', '"+password+"');");
		stmt.close();
	}
	
	public static boolean login(String username, String password) throws SQLException {
		if(con == null || con.isClosed())
			return false;
		String query = "select * from utenti where username=? and password=?;";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);
		stmt.setString(2, password);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			stmt.close();
			return true;
		}
		stmt.close();
		return false;
	}
	
}