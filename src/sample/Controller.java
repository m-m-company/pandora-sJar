package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

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
    	
    }
    
    public String choosedFile() {
    	FileChooser fileChooser = new FileChooser();
    	String path = fileChooser.showOpenDialog(null).getAbsolutePath();
    	return path;
    }
}
