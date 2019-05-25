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
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AppController {
    @FXML
    private MenuItem newFile;
    @FXML
    private TableView<String> gameList;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Button register;
    @FXML
    private Button login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    
    public void init() {
    	//TODO: da fare
    }
    
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
			if(DBConnection.login(user, pass))
				System.out.println("DATI CORRETTI");
			else
				System.out.println("DATI NON CORRETTI");
			DBConnection.chiudiConnessione();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public String choosedFile() {
    	FileChooser fileChooser = new FileChooser();
    	String path = fileChooser.showOpenDialog(null).getAbsolutePath();
    	return path;
    }
}
