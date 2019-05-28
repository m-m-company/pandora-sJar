package controllers;

import database.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class AppController{

    @FXML
    private FlowPane gameList;

    @FXML
    private Label username;
    
    @FXML
    private ImageView propic;
    
    public void init(User actualUser) {
    	username.setText(actualUser.getUsername());
    	propic.setImage(actualUser.getImage());
    }

}
