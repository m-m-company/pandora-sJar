package controllers;

import java.io.File;

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
    public void initialize() {
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void init() {
        File f = new File(game.getPath());
        File h = new File(f.getParent() + File.separator + "preview.png");
        Image img = new Image(h.getPath(), 80, 80, false, false);
        gameImage.setImage(img);
        name.setText(game.getName());
    }

}
