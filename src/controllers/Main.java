package controllers;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class Main extends Application {
	public static final String viewPath = ".."+File.separator+"view"+File.separator;
	public static final String controllersPath = ".."+File.separator+"controllers"+File.separator;
	public static final String resourcesPath = "resources" + File.separator;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader controllerA = new FXMLLoader(getClass().getResource(viewPath+"Login.fxml"));
    	AnchorPane root = controllerA.load();
        primaryStage.setTitle("Login");
        Image back = new Image("file:"+resourcesPath+"/background.png");
        BackgroundImage backgroundImage = new BackgroundImage(back, BackgroundRepeat.REPEAT, 
        		BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));
        primaryStage.setScene(new Scene(root, primaryStage.getHeight(), primaryStage.getWidth()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
