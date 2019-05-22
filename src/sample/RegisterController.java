package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Properties;
import java.util.Date;
import java.util.regex.Pattern;

import javax.mail.*;

import javax.mail.internet.*;

import com.sun.mail.smtp.*;
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

	private void sendEmail(){
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
			msg.setText("Welcome to Pandora's Jar my dear "+username.getText()+"!");
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
	private boolean testEmail(String input) {
		return Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\" +
                ".[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", input ) ;
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
	public void refuse(ActionEvent e) {
        Stage th = (Stage) username.getScene().getWindow();
        th.close();
	}
	public RegisterController() {

	}

}
