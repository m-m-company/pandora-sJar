package controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AboutUsController {
	
	@FXML
    private TextArea text;
	
	@FXML
	private Hyperlink link;

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
    
    public void clickLink(ActionEvent event) {
    	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    	    try {
				Desktop.getDesktop().browse(new URI(link.getText()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
    	}
    }

}
