package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DBConnection;
import model.EmailManager;
import model.User;

public class LoginController {
	@FXML
	private ImageView background;
	
	@FXML
	private Label errorData;

	@FXML
	private PasswordField password;

	@FXML
	private Button login;

	@FXML
	private Button register;

	@FXML
	private TextField username;

	@FXML
	private Label clickHere;

	@FXML
	private CheckBox showPassword;

	@FXML
	private TextField passwordText;

	private User actualUser;

	@FXML
	public void initialize() {
		passwordText.managedProperty().bind(showPassword.selectedProperty());
		passwordText.visibleProperty().bind(showPassword.selectedProperty());
		password.managedProperty().bind(showPassword.selectedProperty().not());
		password.visibleProperty().bind(showPassword.selectedProperty().not());
		passwordText.textProperty().bindBidirectional(password.textProperty());
	}

	@FXML
	public void registration(ActionEvent e) {
		AnchorPane root;
		try {
			root = (AnchorPane) FXMLLoader.load(getClass().getResource(Main.viewPath + "Register.fxml"));
			Image back = new Image("file:"+Main.resourcesPath+"/background.png");
	        BackgroundImage backgroundImage = new BackgroundImage(back, BackgroundRepeat.REPEAT, 
	        		BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
	        root.setBackground(new Background(backgroundImage));
			Stage stage = new Stage();
			stage.setTitle("Registration");
			stage.setResizable(false);
			stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
			stage.show();
		} catch (IOException ed) {
			ed.printStackTrace();
		}
	}

	@FXML
	void loginAction(ActionEvent event) {
		String user = username.getText();
		String pass = password.getText();
		try {
			actualUser = DBConnection.inst().login(user, pass);
			if (actualUser != null) {
				errorData.setVisible(false);
				BorderPane root;
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.viewPath + "App.fxml"));
					root = (BorderPane) loader.load();
					AppController controller = loader.getController();
					controller.init(actualUser);
					Stage stage = new Stage();
					stage.setTitle("Pandor's jar");
					stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
						
						@Override
						public void handle(WindowEvent event) {
							controller.logout(null);
						}
					});
					Image back = new Image("file:" + Main.resourcesPath + "/backgroundApp.png");
					BackgroundImage backgroundImage = new BackgroundImage(back, BackgroundRepeat.REPEAT,
							BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
					root.setBackground(new Background(backgroundImage));

					stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
					Stage th = (Stage) username.getScene().getWindow();
					th.close();
					stage.show();
				} catch (IOException ed) {
					ed.printStackTrace();
				}
			} else
				errorData.setVisible(true);
			DBConnection.inst().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void forgotPassword(MouseEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("Insert your email");
		Optional<String> email = dialog.showAndWait();
		String password = null;
		if (email.isPresent()) {
			try {
				password = DBConnection.inst().findPassword(email.get());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (password == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("The email doesen't exist");
			alert.showAndWait();
		} else
			EmailManager.inst().sendPassword(email.get(), password, dialog);
	}

	@FXML
	void enterRegister(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			registration(null);
	}

	@FXML
	void enterLogin(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			loginAction(null);
	}

	@FXML
	void exit(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			Stage th = (Stage) username.getScene().getWindow();
			th.close();
		}
	}

	@FXML
	void effectOn(MouseEvent event) {
		clickHere.setEffect(new Glow(1));
	}

	@FXML
	void effectOff(MouseEvent event) {
		clickHere.setEffect(null);
	}

	@FXML
	void enterShowPassword(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (showPassword.isSelected())
				showPassword.setSelected(false);
			else
				showPassword.setSelected(true);
		}
	}

}
