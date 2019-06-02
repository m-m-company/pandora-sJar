package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class AddGameController {
    @FXML
    public TextField name;
    @FXML
    public TextField path;

    public void submit(ActionEvent actionEvent) {

    }

    public void browse(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
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
