package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Game;

public class GameController extends AnchorPane {
    @FXML
    public ImageView gameImage;
    @FXML
    public Label name;
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public void init() {
        gameImage.setImage(new Image(game.getPath()));
        name.setText(game.getName());
    }

}
