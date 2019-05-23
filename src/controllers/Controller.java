package controllers;

import java.io.File;
import java.io.IOException;

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

public class Controller {
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
            root = FXMLLoader.load(getClass().getResource(".."+File.separator+"view"+File.separator+"Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
        }
        catch (IOException ed) {
            ed.printStackTrace();
        }
    }
    
    public String choosedFile() {
    	FileChooser fileChooser = new FileChooser();
    	String path = fileChooser.showOpenDialog(null).getAbsolutePath();
    	return path;
    }
}
