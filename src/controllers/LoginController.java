package controllers;

import java.io.IOException;
import java.sql.SQLException;

import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button register;

    @FXML
    private TextField username;

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
    		DBConnection.creaConnessione();
			if(DBConnection.login(user, pass)) {
				Parent root;
		        try {
		            root = FXMLLoader.load(getClass().getResource(Main.viewPath+"MainApp.fxml"));
		            Stage stage = new Stage();
		            stage.setTitle("Pandor's jar");
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
				System.out.println("DATI NON CORRETTI");
			DBConnection.chiudiConnessione();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void forgotPassword(ActionEvent event) {
 
    }

    @FXML
    void enterFromField(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		loginAction(null);
    }

}
