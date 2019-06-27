package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
	private ImageView preview;

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
			stage.setResizable(false);
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
		try {
			actualGame.setRanks(DBConnection.inst().getPoints(actualGame));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		actualGame.getRanks().sort(new Comparator<Pair<String, Integer>>() {
			@Override
			public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
				return Integer.compare(o2.getSecond(), o1.getSecond());
			}
		});
		for (int i = 0; i < 5; i++) {
			try {
				Label userName = new Label(i + 1 + ". " + actualGame.getRanks().get(i).getFirst());
				Label points = new Label(actualGame.getRanks().get(i).getSecond().toString());
				gridPane.addRow(i, userName, points);
			} catch (IndexOutOfBoundsException a) {
				i = 5;
			}
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
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPreview(Game game) {
		//windows should not work
		String path = game.getPath().substring(5);
		File f = new File(path); 
		File h = new File(f.getParent()+File.separator+"preview.png");
		Image image = new Image("file:" + h.getPath());
		preview.setImage(image);
		refreshRanks();
	}

	@FXML
	void shortcut(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			this.logout(null);
		}
		if (event.isControlDown() && event.getCode() == KeyCode.N) {
			openAddGame();
		}
	}

	@FXML
	void play(ActionEvent e) {
		if (actualGame != null) {
			String pathGame;
			String so = System.getProperty("os\n" + 
					"\n" + 
					"    @FXML\n" + 
					"    void setGameNull(MouseEvent event) {\n" + 
					"    	actualGame = null;\n" + 
					"    	refreshRanks();\n" + 
					"    }.name");
			if (so.contains("Windows"))
				pathGame = actualGame.getPath().substring(6);
			else
				pathGame = actualGame.getPath().substring(5);
			ProcessBuilder pb = null;
			try {
				String highUser;
				Integer highScore;
				try {
					Pair<String, Integer> record = actualGame.getRanks().get(0);
					highUser = record.getFirst();
					highScore = record.getSecond();
				} catch (IndexOutOfBoundsException a) {
					highUser = actualUser.getUsername();
					highScore = 0;
				}
				pb = new ProcessBuilder("java", "-jar", pathGame, "1");
				if (highUser != null && highScore != null) {
					pb.environment().put("pandoras_HighUser", highUser);
					pb.environment().put("pandoras_HighScore", Integer.toString(highScore));
				} else {
					pb.environment().put("pandoras_HighUser", "YOU");
					pb.environment().put("pandoras_HighScore", "0");
				}
				pb.environment().put("pandoras_ActualUser", actualUser.getUsername());
				String pathPoints = new File(pathGame).getParent();
				File pointsFile = new File(pathPoints + File.separator + "points.txt");
				pb.redirectOutput(pointsFile);
				Process p = pb.start();
				p.waitFor();
				BufferedReader bf = new BufferedReader(new FileReader(pointsFile));
				while (bf.ready()) {
					Integer points = Integer.valueOf(bf.readLine());
					DBConnection.inst().insertPoints(actualGame, actualUser, points);
				}
				refreshRanks();
				PrintWriter pw = new PrintWriter(pointsFile);
				pw.write("");
				pw.close();
				bf.close();
			} catch (SQLException | IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

//	@FXML
//	void enterAddGame(KeyEvent event) {
//		if (event.getCode() == KeyCode.ENTER)
//			openAddGame();
//	}

	
	@FXML
	void enterManageAccount(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			manageAccount(null);
	}

	@FXML
	void enterPlay(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			play(null);
	}

	@FXML
	void removeGame(ActionEvent event) {
		if (actualGame != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("ARE YOU SURE?");
			alert.setContentText("Are you sure to delete this game? You will LOSE EVERY RANKS and this action is " +
					"IRREVERSIBLE");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent()) {
				if (result.get().equals(ButtonType.OK)) {
					try {
						DBConnection.inst().removeGame(actualGame);
						actualGame = null;
						this.refreshGamesList();
						gridPane.getChildren().clear();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@FXML
	void enterRemoveGame(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			removeGame(null);
	}
	
	@FXML
    void logout(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("ARE YOU SURE?");
		alert.setContentText("Are you sure logout?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			if (result.get().equals(ButtonType.OK)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"Login.fxml"));
		    	Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        Stage stage = new Stage();
		        stage.setTitle("Pandor's jar");
		        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
		        stage.setResizable(false);
		        Stage th = (Stage) username.getScene().getWindow();
		        th.close();
		        stage.show();
			}
		}
    }

    @FXML
    void enterLogout(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		logout(null);
    }

    @FXML
    void enterOpenAddGame(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		openAddGame();
    }
    
    @FXML
    void aboutUs(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath+"AboutUs.fxml"));
    	Parent root = null;
    	try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Stage stage = new Stage();
    	stage.setTitle("m-m company");
    	stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
    	stage.setResizable(false);
    	stage.show();
    }
    
    @FXML
    void enterAboutUs(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		aboutUs(null);
    }
    
}
