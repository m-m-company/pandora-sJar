package database;

import javafx.scene.image.Image;

public class User {
	
	private String username;
	private Image image;
	private String email;
	
	public User(String username, String imagePath, String email) {
		super();
		this.username = username;
		this.image = new Image(imagePath);
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getEmail() {
		return email;
	}

}
