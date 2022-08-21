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

    public void initialize(URL url, ResourceBundle rb) {
        // set up combo boxes!
    }

    public void setData(Customer customer) {
        // set all fields
        try {
            this.customer = customer;
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
            // set combo boxes
            countryComboBox.setItems(COUNTRY_DAO.getAllCountries());
            countryComboBox.getSelectionModel().select(customer.getCountry());
            divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(customer.getCountry()));
            divisionComboBox.getSelectionModel().select(customer.getDivision());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.CUSTOMERS, USER);
    }

    public void editButtonPressed(ActionEvent event) {
        nameField.setDisable(false);
        phoneField.setDisable(false);
        addressField.setDisable(false);
        postalCodeField.setDisable(false);
        countryComboBox.setDisable(false);
        divisionComboBox.setDisable(false);
        saveButton.setDisable(false);
        editButton.setDisable(true);
    }

    public void countryComboBoxSelectionChange(ActionEvent event) {
        String newCountry = countryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(newCountry);
        divisionComboBox.setItems(COUNTRY_DAO.getCountryDivisions(newCountry));
        divisionComboBox.getSelectionModel().select(0);
    }

    public void saveButtonPressed() {
        // Func to check for changes and only update those fields?
        System.out.println(USER);
        boolean success = CUSTOMER_DAO.updateCustomer(customer, USER);
        if (!success) {
            // Alert user
            // Refresh data based on updated information!! (just call setData with the modified customer)
        }
    }
}