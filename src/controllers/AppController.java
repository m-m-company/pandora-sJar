package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import model.DBConnection;
import model.Game;
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

	private User actualUser;

	@FXML
	public void initialize() {
	}

	@FXML
	public void add(Game g){
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"Game.fxml"));
		GameController controller = loader.getController();
		controller.setGame(g);
		controller.init();
		Parent root;
		try {
			root = loader.load();
			gameList.getChildren().add((Node) root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openAddGame(){
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"AddGame.fxml"));
		AddGameController gameController = loader.getController();
		gameController.setApp(this);
		try {
			root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add a game");
			stage.setScene(new Scene(root, 300, 500));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void refreshGamesList(){
		try {
			if(gameList.getChildren().size() >0){
				gameList.getChildren().clear();
			}
			ArrayList<Game> games = DBConnection.inst().getGames();
			for (Game g : games){
				add(g);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



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
