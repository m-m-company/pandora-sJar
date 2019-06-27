package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AboutUsController {

    @FXML
    void ok(ActionEvent event) {
    	
    }

    @FXML
    void enterOk(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		ok(null);
    }

}
