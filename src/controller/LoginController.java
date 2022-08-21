package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label titleLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label zoneLabel;

    public void initialize(URL url, ResourceBundle rb) {

        Locale locale = Locale.getDefault();
        Locale.setDefault(locale);
        ZoneId zone = ZoneId.systemDefault();

        rb = ResourceBundle.getBundle("language/login", Locale.getDefault());

        titleLabel.setText(rb.getString("title"));
        loginButton.setText(rb.getString("login"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        zoneLabel.setText(String.valueOf(zone));
    }

    public void login(ActionEvent event) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());

        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isValidUserWithCorrectPassword = false;
        try {
            isValidUserWithCorrectPassword = USER_DAO.verifyUser(username, password);
        } catch (Exception e) {
            System.out.println("LoginController.login: UserDAO.verifyUser did not return a ResultSet object");
            e.printStackTrace();
        }
        if (isValidUserWithCorrectPassword) {
            // Alert of appointments within +- 15 min here?
            SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.HOME, username);
        } else {
            // alert
            Alert incorrectLogin = new Alert(Alert.AlertType.WARNING);
            incorrectLogin.setTitle(rb.getString("loginFailureTitle"));
            incorrectLogin.setContentText(rb.getString("loginFailureMessage"));
            incorrectLogin.showAndWait();
        }
    }
}
