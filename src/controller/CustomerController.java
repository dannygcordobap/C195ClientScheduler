package controller;

import constants.ScenePathConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scheduler.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController extends Controller {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> divisionComboBox;

    @FXML
    private Label titleLabel;

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

    private Customer customer;

    private boolean isNewCustomer;

    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Sets the customer data in the view
     *
     * @param customer - Customer to view
     */
    public void setData(Customer customer) {
        if (customer.getName() == null) {
            isNewCustomer = true;
            changeButtonMode();
        } else {
            isNewCustomer = false;
        }
        this.customer = customer;
        setComboBoxes();
        setTextFields();
    }

    /**
     * Back button handler - takes the user back to the customers table view, if changes were made but not saved
     * the user is alerted and asked for confirmation
     *
     * @param event - Event that triggered the button press
     * @throws IOException - Exception thrown when there is an IO error
     */
    public void backButtonPressed(ActionEvent event) throws IOException {
        boolean inEditMode = editButton.isDisable();
        if (inEditMode) {
            updateCustomer();
            if (customerWasEdited() || isNewCustomer) {
                boolean confirmed = ALERTING.confirm(
                        "Unsaved Changes",
                        "Unsaved changes have been detected in the workspace.\nWould you like to discard these changes?",
                        "Press OK to discard the changes\nPress cancel to return to edit mode"
                );
                if (confirmed) {
                    SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMERS, USER);
                } else {
                    return;
                }
            }
        }
        SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMERS, USER);
    }

    /**
     * Edit button handler - enables the reusing of the page for creation, viewing, editing but changing
     * the disable state of certain buttons.
     */
    public void editButtonPressed(ActionEvent event) {
        changeButtonMode();
    }

    /**
     * Country combo box handler - on change it sets the division combo box appropriately
     */
    public void countryComboBoxSelectionChange() {
        String newCountry = countryComboBox.getSelectionModel().getSelectedItem();
        divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(newCountry));
        divisionComboBox.getSelectionModel().select(0);
    }

    /**
     * Save button handler - validates input and displays a variety of alerts and popups to inform the user
     * of the status of the process, whether or not the process has succeeded, and and invalid inputs.
     */
    public void saveButtonPressed() {
        List<String> invalidFields = getInvalidInputs();
        updateCustomer();
        if (invalidFields.size() > 0) {
            ALERTING.alert(
                    "Invalid Fields",
                    String.format(
                            "Invalid data was detected and the changes were not saved.\n" +
                                    "Please review the following fields: %s",
                            String.join(", ", invalidFields)
                    )
            );
        } else {
            boolean confirmed = ALERTING.confirm(
                    "Save Customer",
                    "Are you sure you would like to save the changes to the customer?",
                    "Press OK to save the changes\nPress Cancel to discard the changes"
            );
            if (confirmed && isNewCustomer) {
                saveCustomer(true);
            } else if (confirmed) {
                if (customerWasEdited()) {
                    saveCustomer(false);
                } else {
                    ALERTING.alert(
                            "No Changes Detected",
                            "Changes were not saved as no changes were detected in the workspace"
                    );
                }
            }
        }
    }

    /**
     * Helper function that saves the customer in the database and handles user alerting
     */
    private void saveCustomer(boolean isNewCustomer) {
        String successTitle = "Success";
        String successMessage;
        String errorTitle;
        String errorMessage;
        boolean success;
        if (isNewCustomer) {
            success = CUSTOMER_DAO.insertCustomer(customer, USER);
            successMessage = "The new customer was successfully added to the system.";
            errorTitle = "Customer Creation Error";
            errorMessage = "There was an error creating the customer.\nPlease wait and try again later.";
        } else {
            success = CUSTOMER_DAO.updateCustomer(customer, USER);
            successMessage = "The customer update was successful and the records have been changed.";
            errorTitle = "Customer Update Error";
            errorMessage = "There was an error updating the customer.\nPlease wait and try again later.";
        }
        if (success) {
            changeButtonMode();
            if (isNewCustomer) {
                customer = CUSTOMER_DAO.getCustomer(customer);
            } else {
                customer = CUSTOMER_DAO.getCustomer(customer.getCustomerId());
            }
            setData(customer);
            ALERTING.inform(successTitle, successMessage);
        } else {
            ALERTING.error(errorTitle, errorMessage);
        }
    }

    /**
     * Helper function to verify whether or not a customer was edited
     *
     * @return - boolean
     */
    private boolean customerWasEdited() {
        Customer customerInDatabase = CUSTOMER_DAO.getCustomer(customer.getCustomerId());
        if (customerInDatabase != null) {
            return !customer.equals(customerInDatabase);
        }
        return true;
    }

    /**
     * Helper function to set the text fields based on customer data
     */
    private void setTextFields() {
        if (isNewCustomer) {
            titleLabel.setText("Adding Customer");
            idLabel.setText("Pending");
            dateCreatedLabel.setText("Not applicable");
            createdByLabel.setText("Not applicable");
            dateUpdatedLabel.setText("Not applicable");
            updatedByLabel.setText("Not applicable");
        } else {
            titleLabel.setText(customer.getName());
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhone());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());

            idLabel.setText(String.valueOf(customer.getCustomerId()));
            dateCreatedLabel.setText(formatDatetime(customer.getCreateDate()));
            createdByLabel.setText(customer.getCreatedBy());
            dateUpdatedLabel.setText(formatDatetime(customer.getLastUpdatedDate()));
            updatedByLabel.setText(customer.getLastUpdatedBy());
        }
    }

    /**
     * Sets the country and division combo boxes based on customer information. If the customer is new then the
     * first country and its first division are selected in the combo boxes
     */
    private void setComboBoxes() {
        try {
            countryComboBox.setItems(COUNTRY_DAO.getAllCountries());
            if (isNewCustomer) {
                // Case when creating new customer
                countryComboBox.getSelectionModel().select(0);
                String country = countryComboBox.getSelectionModel().getSelectedItem();
                divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(country));
                divisionComboBox.getSelectionModel().select(0);
            } else {
                // Case when editing existing customer
                countryComboBox.getSelectionModel().select(customer.getCountry());
                divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(customer.getCountry()));
                divisionComboBox.getSelectionModel().select(customer.getDivision());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to update the appointment object based on input
     */
    private void updateCustomer() {
        customer.setName(nameField.getText());
        customer.setPhone(phoneField.getText());
        customer.setAddress(addressField.getText());
        customer.setPostalCode(postalCodeField.getText());
        customer.setCountry(countryComboBox.getSelectionModel().getSelectedItem());
        customer.setDivision(divisionComboBox.getSelectionModel().getSelectedItem());
    }

    /**
     * Helper function to change between edit and view modes
     */
    private void changeButtonMode() {
        boolean status = nameField.isDisable();
        nameField.setDisable(!status);
        phoneField.setDisable(!status);
        addressField.setDisable(!status);
        postalCodeField.setDisable(!status);
        countryComboBox.setDisable(!status);
        divisionComboBox.setDisable(!status);
        saveButton.setDisable(!status);
        editButton.setDisable(status);
    }

    /**
     * Helper function that validates inputs
     *
     * @return - List of invalid fields as strings
     */
    private List<String> getInvalidInputs() {
        List<String> invalidInputs = new ArrayList<>();
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            invalidInputs.add("name");
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            invalidInputs.add("phone");
        }
        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            invalidInputs.add("address");
        }
        if (postalCodeField.getText() == null || postalCodeField.getText().trim().isEmpty()) {
            invalidInputs.add("postal code");
        }
        return invalidInputs;
    }
}