package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import controllers.Main;

public class DBConnection {

	private Connection con = null;
	private static DBConnection db = null;
	
	private DBConnection() {
		try {
			createConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DBConnection inst() {
		if(db == null)
			db = new DBConnection();
		try {
			if(db.con == null || db.con.isClosed())
				db.createConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return db;
	}
	
	private void createConnection() throws SQLException {		
			String url = "jdbc:sqlite:database.db";
            con = DriverManager.getConnection(url);
            createTables();
	}
	
	public void closeConnection() throws SQLException {
		if(con == null)
			return;		
		con.close();
		con = null;
	}
	
	private void createTables() throws SQLException {
		if(con == null || con.isClosed())
			return;
		String query = "CREATE TABLE IF NOT EXISTS " +
				"users(" +
					"id integer primary key autoincrement," +
					"username varchar(50)," +
					"password varchar(50), " +
					"mail varchar(50)," +
					"imagePath varchar(255)" +
				");";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		query = "CREATE TABLE IF NOT EXISTS " +
				"games(" +
					"name varchar(500) primary key," +
					"path varchar(255)" +
				");";
		stmt.executeUpdate(query);
		query = "CREATE TABLE IF NOT EXISTS " +
				"ranks(" +
					"idplayer integer," +
					"game varchar(500)," +
					"points integer" +
				");";
		stmt.executeUpdate(query);
		stmt.close();
	}
	
	public void insertUser(String username, String password, String mail) throws SQLException {
		if(con == null || con.isClosed())
			return;		
		String imagePath = "file:" + Main.resourcesPath + "defaultPic.png";
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO users (username,password,mail,imagePath) VALUES('"+username+"', '"+password+"', '"+mail+"', '"+imagePath+"');");
		stmt.close();
	}
	
	public User login(String username, String password) throws SQLException {
		if(con == null || con.isClosed())
			return null;
		String query = "select * from users where username=? and password=?;";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, username);
		stmt.setString(2, password);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString(5));
			User user = new User(rs.getInt(1), rs.getString(2), rs.getString(5), rs.getString(4), rs.getString(3));
			stmt.close();
			return user;
		}
		stmt.close();
		return null;
	}
	
	public void changeDataUser(User user) throws SQLException {
		if(con == null || con.isClosed())
			return;
		String query = "update users set username=?, password=?, mail=?, imagePath=? where id=?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, user.getUsername());
		stmt.setString(2, user.getPassword());
		stmt.setString(3, user.getEmail());
		stmt.setString(4, user.getImagePath());
		System.out.println(user.getImagePath());
		stmt.setInt(5, user.getId());
		stmt.executeUpdate();
	}

	public void insertGame(Game g) throws SQLException {
		String query = "Insert into games values (?,?)";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, g.getName());
		pstmt.setString(2, g.getPath());
		pstmt.execute();
		pstmt.close();
	}

	public ArrayList<Game> getGames() throws SQLException {
		ArrayList<Game> games = new ArrayList<>();
		String query = "Select * from games";
		Statement stmt = con.createStatement();
		String subQuery = "Select username,points" +
				"From users,ranks,games" +
				"		Where ranks.game=? and users.id = ranks.idplayer and games.name = ranks.game";
		PreparedStatement pstmt = con.prepareStatement(subQuery);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			pstmt.setString(1, rs.getString(1));
			ResultSet ranks = pstmt.executeQuery();
			HashMap<String, Integer> rank = new HashMap<>();
			while(ranks.next()){
				rank.put(ranks.getString(1), ranks.getInt(3));
			}
			games.add(new Game(rs.getString(1),rs.getString(2), rank));
		}
		stmt.close();
		pstmt.close();
		return games;
	}



}