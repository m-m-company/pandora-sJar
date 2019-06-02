package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Game;

public class GameController extends AnchorPane {
    @FXML
    public ImageView gameImage;
    @FXML
    public Label name;
    private Game game;
    @FXML
    public void initialize() {
        gameImage.setImage(new Image("file:" + Main.resourcesPath + "defaultPic.png"));
    }
    @FXML
    public void prova(MouseEvent e){
        System.out.println("asudyjga");
    }
}
