package model;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;

public class EmailManager {
	
	private static EmailManager em = null;
	
	public static EmailManager inst() {
		if(em == null)
			em = new EmailManager();
		return em;
	}
	
	public void sendRegistrationEmail(String code, String email, String username, TextInputDialog dialog) {
		System.out.println(code);
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
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
					msg.setSubject("WELCOME!");
					msg.setText(
							"Welcome to Pandora's Jar dear " + username + "!" + "\n Your code is: " + code);
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
	
	public void sendPassword(String email, String password, TextInputDialog dialog) {
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
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
					msg.setSubject("PASSWORD RECOVERY");
					msg.setText(
							"Your password is: " + password);
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
	
	public boolean testEmail(String email) {
		return Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", email);
	}

}
