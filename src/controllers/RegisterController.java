package controllers;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class RegisterController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField confirmPassword;
	@FXML
	private TextField email;
	private String code;
	private String generateCode() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 7;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) 
					random.nextFloat() * (rightLimit - leftLimit + 1);
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
	}
	private void sendEmail(){
		code = generateCode();
		Properties props = System.getProperties();
		props.put("mail.smtps.host","smtp.gmail.com");
		props.put("mail.smtps.auth","true");
		Session session = Session.getInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("noreplypandorasjar@gmail.com"));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email.getText(), false));
			msg.setSubject("WELCOME!");
			msg.setText("Welcome to Pandora's Jar my dear "+username.getText()+"!"
					+ "\n Your code is: "+code);
			msg.setHeader("X-Mailer", "");
			msg.setSentDate(new Date());
			SMTPTransport t =
					(SMTPTransport)session.getTransport("smtps");
			t.connect("smtp.gmail.com", "noreplypandorasjar@gmail.com", "otxpncvzndrtbaie");
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}
	private boolean testEmail(String email) {
		return Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\" +
                ".[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", email) ;
	}
	public void sendData(ActionEvent e) {
		if(password.getText().equals(confirmPassword.getText())) {
			if(testEmail(email.getText()) && !username.getText().equals("")) {
                sendEmail();
                Stage th = (Stage) username.getScene().getWindow();
                th.close();
            }
		}
	}
	private boolean showConfirmDialog() {
		TextInputDialog dialog = new TextInputDialog("walter");
		dialog.setTitle("Confirm your code!");
		dialog.setHeaderText("A code has been sent to your email address");
		dialog.setContentText("Please enter your code:");
		Optional<String> result;
		do {
		 result = dialog.showAndWait();
		}while(!result.equals(code));
		return true;
	}
	public void refuse(ActionEvent e) {
        Stage th = (Stage) username.getScene().getWindow();
        th.close();
	}
	public RegisterController() {

	}

}
