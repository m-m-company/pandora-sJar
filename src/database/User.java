package database;

import javafx.scene.image.Image;

public class User {
	
	private int id;
	private String username;
	private Image image;
	private String imagePath;
	private String email;
	private String password;
	
	public User(int id, String username, String imagePath, String email, String password) {
		this.id = id;
		this.username = username;
		this.image = new Image(imagePath);
		this.imagePath = imagePath;
		this.email = email;
		this.password = password;
	}
	
	public User(User u) {
		this.username = u.username;
		this.imagePath = u.imagePath;
		this.image = u.image;
		this.email = u.email;
		this.password = u.password;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		this.setImage(this.imagePath);
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImage(String path) {
		image = new Image(path);
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

}
