package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.Main;

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
		String query = "CREATE TABLE IF NOT EXISTS utenti(username varchar(50), password varchar(50), mail varchar(50), imagePath varchar(255));";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
	
	public static void inserisciDati(String username, String password, String mail) throws SQLException {
		if(con == null || con.isClosed())
			return;		
		String imagePath = "file:" + Main.resourcesPath + "defaultPic.png";
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO utenti VALUES('"+username+"', '"+password+"', '"+mail+"', '"+imagePath+"');");
		stmt.close();
	}
	
	public static User login(String username, String password) throws SQLException {
		if(con == null || con.isClosed())
			return null;
		String query = "select * from utenti where username=? and password=?;";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);
		stmt.setString(2, password);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			User user = new User(rs.getString(1), rs.getString(4), rs.getString(3));
			stmt.close();
			return user;
		}
		stmt.close();
		return null;
	}
	
}