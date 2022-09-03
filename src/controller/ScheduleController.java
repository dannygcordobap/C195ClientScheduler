package controller;

import constants.ScenePathConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.Appointment;
import scheduler.Customer;
import scheduler.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    }

    @Override
    public void setUser(User user) {
        USER = user;
        updateAppointments();
    }

    public void updateAppointments() {
        appointmentTable.setItems(APPOINTMENT_DAO.getUserAppointments(USER));
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.HOME, USER);
    }

    public void createButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.APPOINTMENT, USER, new Appointment());
    }

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

    public void deleteButtonPressed(ActionEvent event) {

        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            boolean confirmed = ALERTING.confirm(
                    "Delete Customer",
                    "You are about to delete an appointment\n" +
                            "This action is irreversible! Are you sure you would like to continue?",
                    "Press OK to delete the appointment\nPress Cancel to cancel deletion."
            );
            if (confirmed) {
                // TODO: Delete customer appointments -> Delete customer by ID
                int selectedCustomerId = selectedAppointment.getCustomerId();
                boolean success = (
                        CUSTOMER_DAO.deleteCustomer(selectedCustomerId) &&
                                APPOINTMENT_DAO.deleteCustomerAppointments(selectedCustomerId)
                );
                if (success) {
                    updateAppointments();
                    ALERTING.inform(
                            "Successful Delete",
                            "The selected appointment was successfully deleted."
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
