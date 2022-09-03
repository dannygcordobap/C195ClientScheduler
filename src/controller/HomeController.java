package controller;

import constants.ScenePathConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends Controller {

    @FXML
    private Button scheduleButton;

    @FXML
    private Button customerButton;

    public void initialize(URL url, ResourceBundle rb) {

    }

    public void scheduleButtonClicked(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.SCHEDULE, USER);
    }

    public void customerButtonClicked(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMERS, USER);
    }
}
