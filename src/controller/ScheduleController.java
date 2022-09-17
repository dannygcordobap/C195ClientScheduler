package controller;

import constants.ScenePathConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import scheduler.Appointment;
import scheduler.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ScheduleController extends Controller {

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button deleteButton;

    @FXML
    private RadioButton weeklyViewRadioButton;

    @FXML
    private RadioButton monthlyViewRadioButton;

    @FXML
    private TableColumn<Appointment, String> titleTVCol;

    @FXML
    private TableColumn<Appointment, String> customerTVCol;

    @FXML
    private TableColumn<Appointment, String> descriptionTVCol;

    @FXML
    private TableColumn<Appointment, String> locationTVCol;

    @FXML
    private TableColumn<Appointment, String> typeTVCol;

    @FXML
    private TableColumn<Appointment, String> startTVCol;

    @FXML
    private TableColumn<Appointment, String> endTVCol;

    @FXML
    private TableColumn<Appointment, String> contactNameTVCol;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private final ToggleGroup viewToggleGroup = new ToggleGroup();


    /**
     * The view's initialization function that uses lambda functions to set the table view cell value factories
     */
    public void initialize(URL url, ResourceBundle rb) {
        titleTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        customerTVCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        CUSTOMER_DAO.getCustomer(cellData.getValue().getCustomerId()).getName()
                )
        );
        descriptionTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        typeTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        startTVCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        formatDatetime(cellData.getValue().getStart())
                )
        );
        endTVCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        formatDatetime(cellData.getValue().getEnd())
                )
        );
        contactNameTVCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        CONTACT_DAO.getContact(cellData.getValue().getContactId()).getContactName()
                )
        );
        weeklyViewRadioButton.setToggleGroup(viewToggleGroup);
        monthlyViewRadioButton.setToggleGroup(viewToggleGroup);
        viewToggleGroup.selectToggle(weeklyViewRadioButton);
    }

    /**
     * Overridden setUser function that updates the appointments after the user has been set
     *
     * @param user - current logged in user
     */
    @Override
    public void setUser(User user) {
        USER = user;
        updateAppointments();
    }

    /**
     * Updates the customer table view
     */
    public void updateAppointments() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp end;
        if (viewToggleGroup.getSelectedToggle().equals(weeklyViewRadioButton)) {
            end = new Timestamp(currentTime.getTime() + TimeUnit.DAYS.toMillis(7));
        } else {
            end = new Timestamp(currentTime.getTime() + TimeUnit.DAYS.toMillis(30));
        }
        appointmentTable.setItems(APPOINTMENT_DAO.getUserAppointmentsInRange(USER, currentTime, end));
    }

    /**
     * Back button handler - redirects the user to the home page
     *
     * @param event - Triggering event
     * @throws IOException - Exception thrown when there is an IO error
     */
    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.HOME, USER);
    }

    /**
     * Create button handler - redirects the user to the appointment page passing in an empty customer
     *
     * @param event - Triggering event
     * @throws IOException - Exception thrown when there is an IO error
     */
    public void createButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.APPOINTMENT, USER, new Appointment());
    }

    /**
     * Create button handler - redirects the user to the appointment page passing in the selected appointment
     *
     * @param event - Triggering event
     * @throws IOException - Exception thrown when there is an IO error
     */
    public void viewButtonPressed(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            SCENE_MANAGER.changeScene(event, ScenePathConstants.APPOINTMENT, USER, selectedAppointment);
        } else {
            ALERTING.alert(
                    "No Customer Selected",
                    "No customer has been selected, please select a customer from the table."
            );
        }
    }

    /**
     * Delete button handler - asks the user for verification and deletes the selected appointment
     */
    public void deleteButtonPressed(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            boolean confirmed = ALERTING.confirm(
                    "Delete Appointment",
                    "You are about to delete an appointment\n" +
                            "This action is irreversible! Are you sure you would like to continue?",
                    "Press OK to delete the appointment\nPress Cancel to cancel deletion."
            );
            if (confirmed) {
                if (APPOINTMENT_DAO.deleteAppointment(selectedAppointment.getAppointmentId())) {
                    updateAppointments();
                    String message = String.format(
                            "The selected appointment was successfully deleted.\n\n" +
                                    "APPOINTMENT DELETED:\nAppointment ID: %d,\tType: %s",
                            selectedAppointment.getAppointmentId(),
                            selectedAppointment.getType());
                    ALERTING.inform(
                            "Successful Delete",
                            message
                    );
                } else {
                    ALERTING.alert(
                            "Appointment Delete Error",
                            "There was an error deleting the appointment.\n" +
                                    "Please try again later."
                    );
                }
            }
        } else {
            ALERTING.alert(
                    "No Appointment Selected",
                    "No appointment has been selected, please select a appointment from the table."
            );
        }
    }
}
