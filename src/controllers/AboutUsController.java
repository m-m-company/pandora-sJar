package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AboutUsController {
	
	@FXML
    private Label text;

    @FXML
    void ok(ActionEvent event) {
    	Stage s = (Stage)text.getScene().getWindow();
    	s.close();
    }

    @FXML
    void enterOk(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ok(null);
    }

}
