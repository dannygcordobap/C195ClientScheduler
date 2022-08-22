package controller;

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

    public void backButtonPressed(ActionEvent event) throws IOException {
        boolean inEditMode = editButton.isDisable();
        if (inEditMode){
            boolean confirmed = ALERTING.confirm(
                    "Unsaved Changes",
                    "Unsaved changes have been detected in the workspace.\nWould you like to discard these changes?",
                    "Press OK to discard the changes\nPress cancel to return to edit mode"
            );
            if (confirmed) {
                SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.CUSTOMERS, USER);
            }
        } else {
            SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.CUSTOMERS, USER);
        }
    }

    public void editButtonPressed(ActionEvent event) {
        changeButtonMode();
    }

    public void countryComboBoxSelectionChange(ActionEvent event) {
        String newCountry = countryComboBox.getSelectionModel().getSelectedItem();
        divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(newCountry));
        divisionComboBox.getSelectionModel().select(0);
    }

    public void saveButtonPressed() {
        List<String> invalidFields = getInvalidInputs();
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
                if (customerWasEdited(customer)) {
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

    private void saveCustomer(boolean isNewCustomer) {
        String successTitle = "Success";
        String successMessage;
        String errorTitle;
        String errorMessage;
        boolean success;
        updateCustomerAttributes();
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
            customer = CUSTOMER_DAO.getCustomer(customer);
            setData(customer);
            ALERTING.inform(successTitle, successMessage);
        }
        else {
            ALERTING.error(errorTitle, errorMessage);
        }
    }

    private boolean customerWasEdited(Customer customer) {
        Customer customerInDatabase = CUSTOMER_DAO.getCustomer(customer.getCustomerId());
        if (customerInDatabase != null) {
            if (customer.equals(customerInDatabase)) {
                return false;
            }
            return true;
        }
        else {
            return true;
        }
    }

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
            idLabel.setText(String.valueOf(customer.getCustomerId()));
            phoneField.setText(customer.getPhone());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            dateCreatedLabel.setText(customer.getCreateDate().toString());
            createdByLabel.setText(customer.getCreatedBy());
            dateUpdatedLabel.setText(customer.getLastUpdatedDate().toString());
            updatedByLabel.setText(customer.getLastUpdatedBy());
        }
    }

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

    private void updateCustomerAttributes() {
        customer.setName(nameField.getText());
        customer.setPhone(phoneField.getText());
        customer.setAddress(addressField.getText());
        customer.setPostalCode(postalCodeField.getText());
        customer.setCountry(countryComboBox.getSelectionModel().getSelectedItem());
        customer.setDivision(divisionComboBox.getSelectionModel().getSelectedItem());
    }

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