package controller;

import constants.ScenePathConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.Customer;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController extends Controller {

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Customer, String> nameTVCol;

    @FXML
    private TableColumn<Customer, String> phoneTVCol;

    @FXML
    private TableColumn<Customer, String> divisionTVCol;

    @FXML
    private TableColumn<Customer, String> countryTVCol;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        nameTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        phoneTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        divisionTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDivision()));
        countryTVCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        updateCustomers();
    }

    public void updateCustomers() {
        customerTable.setItems(CUSTOMER_DAO.getCustomers());
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.HOME, USER);
    }

    public void createButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMER, USER, new Customer());
    }

    public void viewButtonPressed(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            SCENE_MANAGER.changeScene(event, ScenePathConstants.CUSTOMER, USER, selectedCustomer);
        } else {
            ALERTING.alert(
                    "No Customer Selected",
                    "No customer has been selected, please select a customer from the table."
            );
        }
    }

    public void deleteButtonPressed(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            boolean confirmed = ALERTING.confirm(
                    "Delete Customer",
                    "You are about to delete a customer!\n" +
                            "This action is irreversible! Are you sure you would like to continue?",
                    "Press OK to delete the customer\nPress Cancel to cancel deletion."
            );
            if (confirmed) {
                // TODO: Delete customer appointments -> Delete customer by ID
                int selectedCustomerId = selectedCustomer.getCustomerId();
                boolean success = (
                        CUSTOMER_DAO.deleteCustomer(selectedCustomerId) &&
                                APPOINTMENT_DAO.deleteCustomerAppointments(selectedCustomerId)
                );
                if (success) {
                    updateCustomers();
                    ALERTING.inform(
                            "Successful Delete",
                            "The selected customer and their appoinments were successfully deleted."
                    );
                } else {
                    ALERTING.alert(
                            "Customer Delete Error",
                            "There was an error deleting the customer and their appointments.\n" +
                                    "Please try again later."
                    );
                }
            }
        } else {
            ALERTING.alert(
                    "No Customer Selected",
                    "No customer has been selected, please select a customer from the table."
            );
        }
    }
}
