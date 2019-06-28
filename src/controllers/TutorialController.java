package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TutorialController {
	
	private final static int IMAGES_NUMBER = 6;
	public static int index = 0;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;
    
    @FXML
    private ImageView imageView;
    
    private Image images[] = new Image[IMAGES_NUMBER];
    
    @FXML
    public void initialize() {
    	for(int i = 0; i < IMAGES_NUMBER; ++i)
    		images[i] = new Image("file:" + Main.resourcesPath + "/tutorial/" + Integer.toString(i) + ".png");
    	imageView.setImage(images[index]);
    }

    @FXML
    void leftButtonAction(ActionEvent event) {
    	if(index - 1 == 0)
    		leftButton.setVisible(false);
    	--index;
    	rightButton.setVisible(true);
    	imageView.setImage(images[index]);
    }

    @FXML
    void enterLeftButton(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		leftButtonAction(null);
    }

    @FXML
    void rightButtonAction(ActionEvent event) {
    	if(index + 1 == IMAGES_NUMBER - 1)
    		rightButton.setVisible(false);
    	++index;
    	leftButton.setVisible(true);
    	imageView.setImage(images[index]);
    }

    @FXML
    void enterRightButton(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		rightButtonAction(null);
    }

}
