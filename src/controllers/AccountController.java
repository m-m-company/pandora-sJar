package controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DBConnection;
import model.EmailManager;
import model.User;

public class AccountController {
	
	@FXML
	private PasswordField oldPassword;
	
	@FXML
	private PasswordField newPassword1;
	
	@FXML
	private PasswordField newPassword2;
	
	@FXML
	private TextField oldPasswordText;
	
	@FXML
	private TextField newPasswordText;
	
	@FXML
	private TextField confirmPasswordText;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    private Label errorPassword1;
    
    @FXML
    private Label errorPassword2;
    
    @FXML
    private Label errorPassword3;

    @FXML
    private Label errorMail;
    
    @FXML
    private CheckBox showPassword1;
    
    @FXML
    private CheckBox showPassword2;
    
    @FXML
    private CheckBox showPassword3;
    
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
    	//checkbox setting
    	oldPasswordText.managedProperty().bind(showPassword1.selectedProperty());
        oldPasswordText.visibleProperty().bind(showPassword1.selectedProperty());
        oldPassword.managedProperty().bind(showPassword1.selectedProperty().not());
        oldPassword.visibleProperty().bind(showPassword1.selectedProperty().not());
        oldPasswordText.textProperty().bindBidirectional(oldPassword.textProperty());
        
        newPasswordText.managedProperty().bind(showPassword2.selectedProperty());
        newPasswordText.visibleProperty().bind(showPassword2.selectedProperty());
        newPassword1.managedProperty().bind(showPassword2.selectedProperty().not());
        newPassword1.visibleProperty().bind(showPassword2.selectedProperty().not());
        newPasswordText.textProperty().bindBidirectional(newPassword1.textProperty());
        
        confirmPasswordText.managedProperty().bind(showPassword3.selectedProperty());
        confirmPasswordText.visibleProperty().bind(showPassword3.selectedProperty());
        newPassword2.managedProperty().bind(showPassword3.selectedProperty().not());
        newPassword2.visibleProperty().bind(showPassword3.selectedProperty().not());
        confirmPasswordText.textProperty().bindBidirectional(newPassword2.textProperty());
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
    	boolean testMail = EmailManager.inst().testEmail(email.getText());
    	boolean match = newPassword1.getText().equals(newPassword2.getText());
    	boolean alreadyExist = false;
    	
    	try {
    		if(!email.getText().equals(user.getEmail()))
    			alreadyExist = DBConnection.inst().emailAlreadyExists(email.getText());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	if(!testMail) {
    		errorMail.setText("email is not valid");
    		errorMail.setVisible(true);
    	}
    	else if(alreadyExist) {
    		errorMail.setText("email already exists");
    		errorMail.setVisible(true);
    	}
    	else
    		errorMail.setVisible(false);
    	
    	if(!oldPassword.getText().equals(user.getPassword()))
    		errorPassword1.setVisible(true);
    	else
    		errorPassword1.setVisible(false);
    	
    	if(newPassword1.getText().isEmpty()) {
    		errorPassword2.setVisible(true);
    	}
    	else
    		errorPassword2.setVisible(false);
    	
    	System.out.println(match);
    	if(!match)
			errorPassword3.setVisible(true);
		else
			errorPassword3.setVisible(false);
    	
    	if(oldPassword.getText().isEmpty() && newPassword1.getText().isEmpty()) {
    		errorPassword1.setVisible(false);
    		errorPassword2.setVisible(false);
    	}
    	
    	//we control only the visibility of the error's label for the confirm action
    	if(!errorMail.isVisible() && !errorPassword1.isVisible() && !errorPassword2.isVisible() && !errorPassword3.isVisible()) {
    		if(!newPassword1.getText().isEmpty())
    			user.setPassword(newPassword1.getText());
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
    
    @FXML
    void enterShowPassword3(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER) {
    		if(showPassword3.isSelected())
    			showPassword3.setSelected(false);
    		else
    			showPassword3.setSelected(true);
    	}
    }
    
}
