package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class Controller {
	
	HashMap<String, ArrayList<String>> genres;
	

    @FXML
    private MenuItem newFile;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;
    
    @SuppressWarnings("unchecked")
	public void init() {
    	genres = new HashMap<String, ArrayList<String>>();
    	try {
			FileInputStream fis = new FileInputStream("resources" + File.separator + "genres.pand");
			ObjectInputStream ois = new ObjectInputStream(fis);
			genres = (HashMap<String, ArrayList<String>>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public String choosedFile() {
    	FileChooser fileChooser = new FileChooser();
    	String path = fileChooser.showOpenDialog(null).getAbsolutePath();
    	return path;
    }

}
