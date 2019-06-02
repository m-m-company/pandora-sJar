package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameController extends AnchorPane {
    @FXML
    public ImageView gameImage;
    @FXML
    public Label name;
    @FXML
    public void initialize() {
        System.out.println("ciao");
        gameImage.setImage(new Image("file:" + Main.resourcesPath + "defaultPic.png"));
        name.setText("hey");
    }

}
