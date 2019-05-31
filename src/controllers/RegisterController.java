package controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

import database.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegisterController {
	
	@FXML
    private Label errorEmail;
	@FXML
    private Label errorPassword1;
    @FXML
    private Label errorPassword2;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField confirmPassword;
	@FXML
	private TextField email;

	private TextInputDialog dialog;
	private String code;

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
		System.out.println(code);
		// I can't use Thread 'cause of JavaFX
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Properties props = System.getProperties();
				props.put("mail.smtps.host", "smtp.gmail.com");
				props.put("mail.smtps.auth", "true");
				Session session = Session.getInstance(props, null);
				Message msg = new MimeMessage(session);
				try {
					msg.setFrom(new InternetAddress("noreplypandorasjar@gmail.com"));
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText(), false));
					msg.setSubject("WELCOME!");
					msg.setText(
							"Welcome to Pandora's Jar dear " + username.getText() + "!" + "\n Your code is: " + code);
					msg.setHeader("X-Mailer", "");
					msg.setSentDate(new Date());
					SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
					t.connect("smtp.gmail.com", "noreplypandorasjar@gmail.com", "otxpncvzndrtbaie");
					t.sendMessage(msg, msg.getAllRecipients());
					System.out.println("Response: " + t.getLastServerResponse());
					t.close();
				} catch (MessagingException ex) {
					dialog.setHeaderText(
							"There are problems with your internet connection, check it, press cancel and try it again");
				}
			}
		});
	}

	private boolean testEmail(String email) {
		return Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\"
				+ ".[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", email);
	}

	public void sendData(ActionEvent e) {
		if (password.getText().equals(confirmPassword.getText())) {
			errorPassword1.setVisible(false);
			errorPassword2.setVisible(false);
			if (testEmail(email.getText()) && !username.getText().equals("")) {
				errorEmail.setVisible(false);
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
			else
				errorEmail.setVisible(true);
		}
		else {
			errorPassword1.setVisible(true);
			errorPassword2.setVisible(true);
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
		if (event.getCode() == KeyCode.ENTER)
			this.sendData(null);
	}

}
