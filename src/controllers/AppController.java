package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.DBConnection;
import model.Game;
import model.User;

public class AppController {

    @FXML
    private FlowPane gameList;
    
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label username;

    @FXML
    private ImageView propic;

    @FXML
    private MediaView preview;

    @FXML
    private Button playButton;

    private User actualUser;
    private Game actualGame;
    @FXML
    public void initialize() {
        refreshGamesList();
        playButton.setGraphic(new ImageView(new Image("file:"+Main.resourcesPath+"playButton.png",40,40,false,false)));
        gameList.prefWidthProperty().bind(scrollPane.widthProperty());
        gameList.prefHeightProperty().bind(scrollPane.heightProperty());
        gameList.setHgap(10f);
        gameList.setVgap(5f);
    }

    public void add(Game g) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath + "Game.fxml"));
        Parent root;
        try {
            root = loader.load();
            GameController controller = loader.getController();
            controller.setGame(g);
            controller.init(this);
            gameList.getChildren().add((Node) root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActualGame(Game g) {
    	this.actualGame = g;
    }
    
    @FXML
    public void openAddGame() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath + "AddGame.fxml"));
        try {
            root = loader.load();
            AddGameController gameController = loader.getController();
            gameController.setApp(this);
            Stage stage = new Stage();
            stage.setTitle("Add a game");
            stage.setScene(new Scene(root, 300, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshGamesList() {
        try {
            if (gameList.getChildren().size() > 0) {
                gameList.getChildren().clear();
            }
            ArrayList<Game> games = DBConnection.inst().getGames();
            for (Game g : games) {
                add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void init(User actualUser) {
        this.actualUser = actualUser;
        refreshAccount();
    }

    public void refreshAccount() {
        username.setText(actualUser.getUsername());
        propic.setImage(actualUser.getImage());
    }

    @FXML
    void manageAccount(ActionEvent event) {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath + "Account.fxml"));
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

	public void showPreview(Game game) {
		System.out.println(game.getPath());
		String path = game.getPath().substring(6);
		File f = new File(path);
		File h = new File(f.getParent()+File.separator+"preview.mp4");
		System.out.println(h.getPath());
        Media media = null;
		try {

			media = new Media(h.toURI().toURL().toExternalForm());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        preview.setMediaPlayer(mediaPlayer);
	}
	
	@FXML
    void shortcut(KeyEvent event) {
		if(event.getCode() == KeyCode.ESCAPE) {
			Stage th = (Stage) username.getScene().getWindow();
			th.close();
		}
		if(event.isControlDown() && event.getCode() == KeyCode.N) {
			openAddGame();
		}
    }
	
	@FXML
	void play(ActionEvent e) {
		StringTokenizer st = new StringTokenizer(actualGame.getPath(),":");
		st.nextToken();
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", st.nextToken(), "1");
		try {
			pb.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
    void enterAddGame(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			openAddGame();
    }
	
	@FXML
    void enterManageAccount(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			manageAccount(null);
    }
	
}
