package controllers;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.DBConnection;
import model.EmailManager;

public class RegisterController {
	
	@FXML
    private Label errorEmail;
	
	@FXML
    private Label errorPassword1;
	
    @FXML
    private Label errorPassword2;
    
    @FXML
    private Label errorUsername;
    
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField confirmPassword;
	
	@FXML
	private TextField textPassword;
	
	@FXML
	private TextField textConfirmPassword;
	
	@FXML
	private CheckBox showPassword1;
	
	@FXML
	private CheckBox showPassword2;
	
	@FXML
	private TextField email;

	private TextInputDialog dialog;
	private String code;

	@FXML
	public void initialize() {
		//checkbox setting
		textPassword.managedProperty().bind(showPassword1.selectedProperty());
		textPassword.visibleProperty().bind(showPassword1.selectedProperty());
		password.managedProperty().bind(showPassword1.selectedProperty().not());
		password.visibleProperty().bind(showPassword1.selectedProperty().not());
		textPassword.textProperty().bindBidirectional(password.textProperty());
		
		textConfirmPassword.managedProperty().bind(showPassword2.selectedProperty());
		textConfirmPassword.visibleProperty().bind(showPassword2.selectedProperty());
		confirmPassword.managedProperty().bind(showPassword2.selectedProperty().not());
		confirmPassword.visibleProperty().bind(showPassword2.selectedProperty().not());
		textConfirmPassword.textProperty().bindBidirectional(confirmPassword.textProperty());
	}
	
	private String generateCode() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 7;
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = ThreadLocalRandom.current().nextInt(leftLimit, rightLimit + 1);
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
	}

	private void sendEmail() {
		code = generateCode();
		EmailManager.inst().sendRegistrationEmail(code, email.getText(), username.getText(), dialog);
	}

	public void sendData(ActionEvent e) {
		if(password.getText().isEmpty()) {
			errorPassword1.setText("password is not valid");
			errorPassword1.setVisible(true);
		}
		else if (!password.getText().equals(confirmPassword.getText())) {
			errorPassword1.setText(errorPassword2.getText());
			errorPassword1.setVisible(true);
			errorPassword2.setVisible(true);
		}
		else {
			errorPassword1.setVisible(false);
			errorPassword2.setVisible(false);
		}
			
		boolean testMail = EmailManager.inst().testEmail(email.getText());
		boolean alreadyExist = false;
		try {
			alreadyExist = DBConnection.inst().emailAlreadyExists(email.getText());
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		if(!testMail) {
			errorEmail.setText("email is not valid");
			errorEmail.setVisible(true);
		}
		else if(alreadyExist) {
			errorEmail.setText("email already exists");
			errorEmail.setVisible(true);
		}
		else
			errorEmail.setVisible(false);
		
		if(username.getText().isEmpty())
			errorUsername.setVisible(true);
		else
			errorUsername.setVisible(false);
		
		if (!errorUsername.isVisible() && !errorPassword1.isVisible()
				&& !errorPassword2.isVisible() && !errorEmail.isVisible()) {
			sendEmail();
			if (showConfirmDialog()) {
				Stage th = (Stage) username.getScene().getWindow();
				showOkDialog();
				th.close();
				try {
					DBConnection.inst().insertUser(username.getText(), password.getText(), email.getText());
					DBConnection.inst().closeConnection();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}	
	}

	private boolean showConfirmDialog() {
		dialog = new TextInputDialog();
		dialog.setTitle("Confirm your code!");
		dialog.setHeaderText("A code has been sent to your email address you will receive it within one minute");
		dialog.setContentText("Please enter your code:");
		Optional<String> result;
		do {
			result = dialog.showAndWait();
			if (!result.isPresent())
				return false;
			if (!result.get().equals(code))
				dialog.setContentText("Code is wrong! Try again!");
		} while (!result.get().equals(code));
		return true;
	}

	private void showOkDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registration Complete!");
		alert.setHeaderText(null);
		alert.setContentText(
				"I have a great message for you! \n" + "Your registration is complete! \n" + "Now you can Login!");
		alert.showAndWait();
	}

	public void refuse(ActionEvent e) {
		Stage th = (Stage) username.getScene().getWindow();
		th.close();
	}

	@FXML
	void confirmAction(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			sendData(null);
	}
	
	@FXML
	void exit(KeyEvent event) {
		if(event.getCode() == KeyCode.ESCAPE) {
			Stage th = (Stage) username.getScene().getWindow();
			th.close();
		}
	}
	
	@FXML
    void enterConfirm(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			sendData(null);
    }
	
	@FXML
    void enterCancel(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			refuse(null);
    }
	
	@FXML
	void enterShowPassword1(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			if(showPassword1.isSelected())
				showPassword1.setSelected(false);
			else
				showPassword1.setSelected(true);
		}
	}
	
	@FXML
	void enterShowPassword2(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			if(showPassword2.isSelected())
				showPassword2.setSelected(false);
			else
				showPassword2.setSelected(true);
		}
	}

}
