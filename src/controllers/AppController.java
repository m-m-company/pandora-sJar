package controllers;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class AppController{

    @FXML
    private FlowPane gameList;

    @FXML
    private Label username;
    
    @FXML
    private ImageView propic;
    
    @FXML
    private MediaView preview;

	@FXML
	public void initialize() {
	}

	@FXML
	public void add(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"Game.fxml"));
		Parent root;
		try {
			root = loader.load();
			GameController controller = loader.getController();
			controller.initialize();
			gameList.getChildren().add((Node) root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private User actualUser;
    
    public void init(User actualUser) {
    	this.actualUser = actualUser;
    	refresh();
    }
    
    public void refresh() {
    	username.setText(actualUser.getUsername());
    	propic.setImage(actualUser.getImage());
    }
    
    @FXML
    void manageAccount(ActionEvent event) {
    	Parent root;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"Account.fxml"));
    	try {
			root = loader.load();
			AccountController controller = loader.getController();
			controller.init(actualUser, this);
			Stage stage = new Stage();
			stage.setTitle("Account");
			stage.setScene(new Scene(root, 400, 500));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
