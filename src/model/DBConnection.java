package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	
	//singleton
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
					"path varchar(255)"+
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
	
	public void removeGame(Game g) throws SQLException {
		String query = "DELETE FROM games WHERE name=?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, g.getName());
		stmt.execute();
		stmt.close();
	}

	public ArrayList<Game> getGames() throws SQLException {
		ArrayList<Game> games = new ArrayList<>();
		String query = "Select * from games";
		Statement stmt = con.createStatement();
		String subQuery = "Select username, points " +
				"From users,ranks,games " +
				"Where  game = ? and id = idplayer and name = game;";
		PreparedStatement pstmt = con.prepareStatement(subQuery);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			pstmt.setString(1, rs.getString(1));
			ResultSet ranks = pstmt.executeQuery();
			ArrayList<Pair<String, Integer>> rank = new ArrayList<>();
			while(ranks.next()){
				rank.add(new Pair<String,Integer>(ranks.getString(1), ranks.getInt(2)));
			}
			games.add(new Game(rs.getString(1),rs.getString(2), rank));
		}
		stmt.close();
		pstmt.close();
		return games;
	}

	public void insertPoints(Game game, User user, Integer points) throws SQLException {
		String query = "Insert into ranks values (?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, user.getId());
		pstmt.setString(2, game.getName());
		pstmt.setInt(3,points);
		pstmt.execute();
		pstmt.close();
	}
	
	//we return an arrayList of the points that every user has made for that game
	public ArrayList<Pair<String, Integer>> getPoints(Game game) throws SQLException {
		ArrayList<Pair<String, Integer>> pairs = new ArrayList<Pair<String,Integer>>();
		String query = "SELECT users.username, ranks.points FROM users, ranks WHERE game=? AND id=idplayer;";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, game.getName());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			pairs.add(new Pair<String, Integer>(rs.getString(1), rs.getInt(2)));
		}
		return pairs;
	}
	
	public String findPassword(String email) throws SQLException {
		String query = "SELECT password " + 
					   "FROM users " + 
					   "WHERE mail=?;";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getString(1);
		}
		else
			return null;
	}

	public boolean emailAlreadyExists(String email) throws SQLException {
		String query = "SELECT mail " + 
					   "FROM users " + 
					   "WHERE mail=?;";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
			return true;
		return false;
	}
	
}




