package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField confirmPassword;
	@FXML
	private TextField email;
	
	public void sendData(ActionEvent e) {
		
	}
	public void refuse(ActionEvent e) {
	}
	public RegisterController() {
		
	}

}
