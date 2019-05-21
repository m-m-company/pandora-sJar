package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader controllerA = new FXMLLoader(getClass().getResource("sample.fxml"));
        VBox root = controllerA.load();
        Controller a = controllerA.getController();
        a.init();
        primaryStage.setTitle("Pandora's jar");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
