package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.DBConnection;
import model.Game;
import model.Pair;
import model.User;

public class AppController {

	@FXML
	private GridPane gridPane;
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
		playButton.setGraphic(
				new ImageView(new Image("file:" + Main.resourcesPath + "playButton.png", 40, 40, false, false)));
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

	public void refreshRanks() {
		gridPane.getChildren().clear();
		actualGame.getRanks().sort(new Comparator<Pair<String, Integer>>() {
			@Override
			public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
				return Integer.compare(o1.getSecond(), o2.getSecond());
			}
		});
		for (int i = 0; i < actualGame.getRanks().size(); i++) {
			Label userName = new Label(actualGame.getRanks().get(i).getFirst());
			Label points = new Label(actualGame.getRanks().get(i).getSecond().toString());
			gridPane.addRow(i, new Label(Integer.toString(i + 1)), userName, points);
		}
		gridPane.setAlignment(Pos.CENTER_RIGHT); // cambia se vuoi
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
		/*
		 * System.out.println(game.getPath()); String path =
		 * game.getPath().substring(6); File f = new File(path); File h = new
		 * File(f.getParent()+File.separator+"preview.mp4");
		 * 
		 * Media media = null; try {
		 * System.out.println(h.toURI().toURL().toExternalForm()); media = new
		 * Media(h.toURI().toURL().toExternalForm()); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } MediaPlayer mediaPlayer =
		 * new MediaPlayer(media); mediaPlayer.setAutoPlay(true); mediaPlayer.play();
		 * preview.setMediaPlayer(mediaPlayer);
		 */
		refreshRanks();
	}

	@FXML
	void shortcut(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			Stage th = (Stage) username.getScene().getWindow();
			th.close();
		}
		if (event.isControlDown() && event.getCode() == KeyCode.N) {
			openAddGame();
		}
	}

	@FXML
	void play(ActionEvent e) {
		Stage me = (Stage) username.getScene().getWindow();
		me.close();
		String pathGame = actualGame.getPath().substring(5);
		String pathPoints = new File(pathGame).getParent();
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", pathGame, "1");
		try {
			File pointsFile = new File(pathPoints + File.separator + "points.txt");
			pb.redirectOutput(pointsFile);
			Process p = pb.start();
			p.waitFor();
			BufferedReader bf = new BufferedReader(new FileReader(pointsFile));
			Integer points = Integer.valueOf(bf.readLine());
			DBConnection.inst().insertPoints(actualGame, actualUser, points);
			refreshRanks();
			me.show();
		} catch (Exception e1) {
			try {
				DBConnection.inst().insertPoints(actualGame, actualUser, 0);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

	@FXML
	void enterAddGame(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			openAddGame();
	}

	@FXML
	void enterManageAccount(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			manageAccount(null);
	}
	
	@FXML
    void enterPlay(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			play(null);
    }

	@FXML
	void removeGame(ActionEvent event) {
		try {
			DBConnection.inst().removeGame(actualGame);
			this.refreshGamesList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    void enterRemoveGame(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER)
			removeGame(null);
    }

	
}
