package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Game;

public class GameController {
    @FXML
    public ImageView gameImage;
    @FXML
    public Label name;
    private Game game;

    @FXML
    public void initialize(){
        System.out.println("i'm in");
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void init() {
        gameImage.setImage(new Image("file:" + Main.resourcesPath + "defaultPic.png"));
        name.setText(game.getName());
    }

}
