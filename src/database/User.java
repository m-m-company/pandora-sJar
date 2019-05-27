package database;

import javafx.scene.image.Image;

public class User {
	
	private String username;
	private Image image;
	
	public User(String username, Image image) {
		super();
		this.username = username;
		this.image = image;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Image getImage() {
		return image;
	}

}
