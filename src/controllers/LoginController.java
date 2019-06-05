package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.input.MouseEvent;
import model.DBConnection;
import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML
	private Label errorData;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button register;

    @FXML
    private TextField username;
    private User actualUser;

    @FXML
    public void registration(ActionEvent e) {
    	Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(Main.viewPath+"Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        }
        catch (IOException ed) {
            ed.printStackTrace();
        }
    }

    @FXML
    void loginAction(ActionEvent event) {
    	String user = username.getText();
    	String pass = password.getText();
    	try {
    		actualUser = DBConnection.inst().login(user, pass);
			if(actualUser != null) {
				errorData.setVisible(false);
				Parent root;
		        try {
		        	FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"App.fxml"));
		        	root = loader.load();
		        	AppController controller = loader.getController();
		        	controller.init(actualUser);
		            Stage stage = new Stage();
		            stage.setTitle("Pandor's jar");
		            stage.setMaximized(true);
		            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
		            Stage th = (Stage) username.getScene().getWindow();
		            th.close();
		            stage.show();
		        }
		        catch (IOException ed) {
		            ed.printStackTrace();
		        }
			}
			else
				errorData.setVisible(true);
			DBConnection.inst().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void forgotPassword(MouseEvent event) {
 
    }

    @FXML
    void enterFromField(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		loginAction(null);
    }
    
    @FXML
    void enterRegister(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		registration(null);
    }
    
    @FXML
    void enterLogin(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		loginAction(null);
    }
    
    @FXML
    void exit(KeyEvent event) {
    	if(event.getCode() == KeyCode.ESCAPE) {
    		Stage th = (Stage) username.getScene().getWindow();
        	th.close();
    	}
    }

}
