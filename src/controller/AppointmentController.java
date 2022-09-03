package controller;

import constants.ScenePathConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scheduler.Appointment;
import scheduler.Contact;
import scheduler.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class AppointmentController extends Controller {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField startDateTimeField;

    @FXML
    private TextField endDateTimeField;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private Label idLabel;

    @FXML
    private Label dateCreatedLabel;

    @FXML
    private Label createdByLabel;

    @FXML
    private Label dateUpdatedLabel;

    @FXML
    private Label updatedByLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    private Appointment appointment;

    private boolean isNewAppointment;

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void saveButtonPressed() {
        List<String> invalidFields = getInvalidInputs();
        updateAppointment();
        if (invalidFields.size() > 0) {
            ALERTING.alert(
                    "Invalid Fields",
                    String.format(
                            "Invalid data was detected and the changes were not saved.\n" +
                                    "Please review the following fields: %s",
                            String.join(", ", invalidFields)
                    )
            );
        } else if (APPOINTMENT_DAO.userHasAppointmentsInRange(
                USER.getUserID(), appointment.getStart(), appointment.getEnd())
        ) {
            ALERTING.alert(
                    "Scheduling Conflicts",
                    "The user has conflicting appointments during the selected time.\n" +
                            "Please review the selected start and end times"
            );
        } else if (APPOINTMENT_DAO.customerHasAppointmentsInRange(
                appointment.getCustomerId(), appointment.getStart(), appointment.getEnd())
        ) {
            ALERTING.alert(
                    "Scheduling Conflicts",
                    "The customer has conflicting appointments during the selected time.\n" +
                            "Please review the selected start and end times"
            );
        } else {
            boolean confirmed = ALERTING.confirm(
                    "Save Customer",
                    "Are you sure you would like to save the changes to the customer?",
                    "Press OK to save the changes\nPress Cancel to discard the changes"
            );
            if (confirmed && isNewAppointment) {
                saveAppointment();
            } else if (confirmed) {
                if (appointmentWasEdited()) {
                    saveAppointment();
                } else {
                    ALERTING.alert(
                            "No Changes Detected",
                            "Changes were not saved as no changes were detected in the workspace"
                    );
                }
            }
        }
    }

    public void editButtonPressed(ActionEvent event) {
        changeButtonMode();
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        boolean inEditMode = editButton.isDisable();
        if (inEditMode) {
            updateAppointment();
            if (appointmentWasEdited() || isNewAppointment) {
                boolean confirmed = ALERTING.confirm(
                        "Unsaved Changes",
                        "Unsaved changes have been detected in the workspace.\nWould you like to discard these changes?",
                        "Press OK to discard the changes\nPress cancel to return to edit mode"
                );
                if (confirmed) {
                    SCENE_MANAGER.changeScene(event, ScenePathConstants.SCHEDULE, USER);
                } else {
                    return;
                }
            }
        }
    SCENE_MANAGER.changeScene(event, ScenePathConstants.SCHEDULE, USER);
    }

    public void setData(Appointment appointment) {
        this.appointment = appointment;
        if (appointment.getTitle() == null) {
            isNewAppointment = true;
            changeButtonMode();
        } else {
            isNewAppointment = false;
        }
        this.appointment = appointment;
        setComboBoxes();
        setTextFields();
    }

    private void saveAppointment() {
        String successTitle = "Success";
        String successMessage;
        String errorTitle;
        String errorMessage;
        boolean success;
        if (isNewAppointment) {
            success = APPOINTMENT_DAO.insertAppointment(appointment, USER);
            successMessage = "The new appointment was successfully added to the system.";
            errorTitle = "Appointment Creation Error";
            errorMessage = "There was an error creating the appointment.\nPlease wait and try again later.";
        } else {
            success = APPOINTMENT_DAO.updateAppointment(appointment, USER);
            successMessage = "The appointment update was successful and the records have been changed.";
            errorTitle = "Appointment Update Error";
            errorMessage = "There was an error updating the appointment.\nPlease wait and try again later.";
        }
        if (success) {
            changeButtonMode();
            appointment = APPOINTMENT_DAO.getAppointment(appointment.getAppointmentId());
            setData(appointment);
            ALERTING.inform(successTitle, successMessage);
        } else {
            ALERTING.error(errorTitle, errorMessage);
        }
    }

    private void setComboBoxes() {
        try {
            customerComboBox.setItems(CUSTOMER_DAO.getCustomers());
            contactComboBox.setItems(CONTACT_DAO.getContacts());
            if (isNewAppointment) {
                // Case when creating new appointment
                customerComboBox.getSelectionModel().select(0);
                contactComboBox.getSelectionModel().select(0);
            } else {
                // Case when editing existing appointment
                customerComboBox.getSelectionModel().select(CUSTOMER_DAO.getCustomer(appointment.getCustomerId()));
                contactComboBox.getSelectionModel().select(CONTACT_DAO.getContact(appointment.getContactId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTextFields() {
        if (isNewAppointment) {
            titleLabel.setText("Adding Appointment");
            idLabel.setText("Pending");
            dateCreatedLabel.setText("Not applicable");
            createdByLabel.setText("Not applicable");
            dateUpdatedLabel.setText("Not applicable");
            updatedByLabel.setText("Not applicable");
        } else {
            titleLabel.setText("Appointment");
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());
            locationField.setText(appointment.getLocation());
            typeField.setText(appointment.getType());
            startDateTimeField.setText(formatDateTimeTextField(appointment.getStart()));
            endDateTimeField.setText(formatDateTimeTextField(appointment.getEnd()));

            idLabel.setText(String.valueOf(appointment.getAppointmentId()));
            createdByLabel.setText(appointment.getCreatedBy());
            dateCreatedLabel.setText(formatDatetime(appointment.getCreateDate()));
            dateUpdatedLabel.setText(formatDatetime(appointment.getLastUpdate()));
            updatedByLabel.setText(appointment.getLastUpdatedBy());
        }
    }

    private void changeButtonMode() {
        boolean status = titleField.isDisable();
        titleField.setDisable(!status);
        descriptionField.setDisable(!status);
        locationField.setDisable(!status);
        typeField.setDisable(!status);
        startDateTimeField.setDisable(!status);
        endDateTimeField.setDisable(!status);
        customerComboBox.setDisable(!status);
        contactComboBox.setDisable(!status);
        saveButton.setDisable(!status);
        editButton.setDisable(status);
    }

    private void updateAppointment() {
        appointment.setTitle(titleField.getText());
        appointment.setDescription(descriptionField.getText());
        appointment.setLocation(locationField.getText());
        appointment.setType(typeField.getText());
        appointment.setStart(timestampFromString(startDateTimeField.getText()));
        appointment.setEnd(timestampFromString(endDateTimeField.getText()));
        appointment.setCustomerId(customerComboBox.getSelectionModel().getSelectedItem().getCustomerId());
        appointment.setContactId(contactComboBox.getSelectionModel().getSelectedItem().getContactId());
    }

    private List<String> getInvalidInputs() {
        List<String> invalidInputs = new ArrayList<>();
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            invalidInputs.add("title");
        }
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            invalidInputs.add("description");
        }
        if (locationField.getText() == null || locationField.getText().trim().isEmpty()) {
            invalidInputs.add("location");
        }
        if (typeField.getText() == null || typeField.getText().trim().isEmpty()) {
            invalidInputs.add("type");
        }
        Timestamp startTimestamp = timestampFromString(startDateTimeField.getText());
        if (startTimestamp == null || timeIsInvalid(startTimestamp)) {
            invalidInputs.add("start date and time");
            invalidTimeRangeAlert();
        }
        Timestamp endTimestamp = timestampFromString(endDateTimeField.getText());
        if (endTimestamp == null || timeIsInvalid(endTimestamp)) {
            invalidInputs.add("end date and time");
            invalidTimeRangeAlert();
        }
        if (startTimestamp != null && endTimestamp != null && startTimestamp.compareTo(endTimestamp) > 0) {
            invalidInputs.add("start date and time");
            invalidInputs.add("end date and time");
            ALERTING.alert(
                    "Invalid Time",
                    "Appointment start date and time cannot be after the end date and time.\n" +
                            "Please review the selected start and end dates and times"
            );
        }
        return invalidInputs;
    }

    private boolean timeIsInvalid(Timestamp timestamp) {
        ZonedDateTime estDateTime = timestamp.toLocalDateTime()
                .atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
        return estDateTime.getHour() < 8 || estDateTime.getHour() > 10;
    }

    private Timestamp timestampFromString(String timestampString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm MM/dd/yy");
            Date parsedDate = dateFormat.parse(timestampString);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException pe) {
            if (!timestampString.isEmpty()) {
                ALERTING.alert(
                        "Invalid Format",
                        "Appointment date and time input must be of the format HH:MM DD/MM/YYY using 24 hour time.\n" +
                                "Please review the selected start and end times"
                );
                return null;
            }
            return null;
        }
    }

    public void invalidTimeRangeAlert() {
        ALERTING.alert(
                "Invalid Time",
                "Appointment times must fall within business hours.\n" +
                        "Business hours: 8 AM - 10 PM EST"
        );
    }

    private boolean appointmentWasEdited() {
        Appointment appointmentInDatabase = APPOINTMENT_DAO.getAppointment(appointment.getAppointmentId());
        if (appointmentInDatabase != null) {
            return !appointment.equals(appointmentInDatabase);
        }
        return true;
    }
}
