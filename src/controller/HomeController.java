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

    @FXML
    private Button reportButton;

    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Schedule button handler - changes scene to the user's schedule view
     *
     * @param event - Triggering event
     * @throws IOException - Is thrown when there is an IO error
     */
    public void scheduleButtonClicked(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.SCHEDULE, USER);
    }

    /**
     * Customer button handler - changes scene to the customers view
     *
     * @param event - Triggering event
     * @throws IOException - Is thrown when there is an IO error
     */
    public void customerButtonClicked(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMERS, USER);
    }

    public void reportButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.REPORTS, USER);
    }
}
