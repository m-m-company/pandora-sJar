package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DBConnection;
import model.Game;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class AddGameController {
    @FXML
    public TextField name;
    @FXML
    public TextField path;

    private AppController app;

    public AppController getApp() {
        return app;
    }

    public void setApp(AppController app) {
        this.app = app;
    }

    public void submit(ActionEvent actionEvent) {
        try {
            DBConnection.inst().insertGame(new Game(name.getText(), path.getText()));
            app.refreshGamesList();
            cancel(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void browse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        try {
            System.out.println("a");
            System.out.println(file.getAbsoluteFile().toURI().toURL().toExternalForm());
            System.out.println("a");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            path.setText(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage th = (Stage) name.getScene().getWindow();
        th.close();
    }
}
