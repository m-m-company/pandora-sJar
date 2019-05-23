package controllers;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader controllerA = new FXMLLoader(getClass().getResource(".."+File.separator+"view"+File.separator+"MainApp.fxml"));
        VBox root = controllerA.load();
        Controller a = controllerA.getController();
        a.init();
        primaryStage.setTitle("Pandora's Jar");
        primaryStage.setScene(new Scene(root, primaryStage.getHeight(), primaryStage.getWidth()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
