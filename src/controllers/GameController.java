package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Game;

import java.io.File;
import java.net.MalformedURLException;

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
        Image img = new Image(h.getPath());
        gameImage.setImage(img);
        name.setText(game.getName());
    }

}
