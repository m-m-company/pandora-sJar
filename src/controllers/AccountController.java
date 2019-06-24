package controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DBConnection;
import model.User;

public class AccountController {
	
	@FXML
	private PasswordField oldPassword;
	
	@FXML
	private PasswordField newPassword;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField email;

    @FXML
    private TextField username;
    
    @FXML
    private Label errorPassword2;

    @FXML
    private Label errorPassword1;

    @FXML
    private Label errorMail;
    
    private User user;
    private User oldUser;
    private AppController app;
    
    public void init(User user, AppController app) {
    	this.app = app;
    	this.user = user;
    	this.oldUser = new User(user);
    	email.setText(user.getEmail());
    	username.setText(user.getUsername());
    	imageView.setImage(user.getImage());
    }
    
    @FXML
    void enterFromEmail(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ConfirmAction(null);
    }

    @FXML
    void enterFromUsername(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ConfirmAction(null);
    }

    @FXML
    void enterFromOld(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ConfirmAction(null);
    }

    @FXML
    void enterFromNew(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ConfirmAction(null);
    }

    @FXML
    void changeImage(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(null);
    	try {
			user.setImagePath(file.toURI().toURL().toExternalForm());
			imageView.setImage(user.getImage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void CancelAction(ActionEvent event) {
    	user = oldUser;
    	Stage th = (Stage) username.getScene().getWindow();
        th.close();
    }

    @FXML
    void ConfirmAction(ActionEvent event) {
    	if(RegisterController.testEmail(email.getText())) {
    		errorMail.setVisible(false);
        	if(!newPassword.getText().isEmpty() && oldPassword.getText().equals(user.getPassword())) {
        		errorPassword1.setVisible(false);
            	errorPassword2.setVisible(false);
        		user.setPassword(newPassword.getText());
            	user.setEmail(email.getText());
            	user.setUsername(username.getText());
            	try {
        			DBConnection.inst().changeDataUser(user);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
            	Stage th = (Stage) username.getScene().getWindow();
                th.close();
                app.refreshAccount();
        	}
        	else {
        		errorPassword1.setVisible(true);
        		errorPassword2.setVisible(true);
        	}
    	}
    	else
    		errorMail.setVisible(true);
    }
    
    @FXML
    void enterCancel(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		CancelAction(null);
    }
    
    @FXML
    void enterConfirm(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ConfirmAction(null);
    }
    
    @FXML
    void enterSelectImage(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		changeImage(null);
    }
    
    @FXML
    void exit(KeyEvent event) {
    	if(event.getCode() == KeyCode.ESCAPE) {
    		Stage th = (Stage) username.getScene().getWindow();
        	th.close();
    	}
    }

}
