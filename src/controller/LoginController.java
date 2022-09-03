package controller;

import constants.ScenePathConstants;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import scheduler.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        boolean isValidUserWithCorrectPassword = USER_DAO.verifyUser(username, password);
        if (isValidUserWithCorrectPassword) {
            setUser(USER_DAO.getUser(username));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (APPOINTMENT_DAO.userHasAppointmentsInRange(
                    USER.getUserID(),
                    currentTime,
                    new Timestamp(currentTime.getTime() + TimeUnit.MINUTES.toMillis(15))
            )) {
                ObservableList<Appointment> appointmentsInRange = APPOINTMENT_DAO.getUserAppointmentsInRange(
                        USER, currentTime, new Timestamp(currentTime.getTime() + TimeUnit.MINUTES.toMillis(15))
                );
                String message = String.format(
                        "You have %d appointments in the next 15 minutes.", appointmentsInRange.size()
                );
                String upcomingAppointments = appointmentsInRange.stream()
                        .map(appointment -> String.format(
                                "%d:\t%s",
                                appointment.getAppointmentId(),
                                formatDatetime(appointment.getStart())
                        ))
                        .collect(Collectors.joining("\n"));
                ALERTING.inform(
                        "Upcoming Appointments",
                        message + "\n\n" + upcomingAppointments
                );
            } else {
                ALERTING.inform(
                        "Upcoming Appointments",
                        "You have no appointments in the next 15 minutes."
                );
            }
            SCENE_MANAGER.changeScene(event, ScenePathConstants.HOME, USER);
        } else {
            ALERTING.alert(rb.getString("loginFailureTitle"), rb.getString("loginFailureMessage"));
        }
    }
}
