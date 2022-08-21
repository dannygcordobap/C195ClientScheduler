package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        try {
            updateCustomers();
        } catch (Exception e) {
            e.printStackTrace();
            Alert noPart = new Alert(Alert.AlertType.WARNING);
            noPart.setTitle("No Part Selected");
            noPart.setHeaderText("No part was selected.");
            noPart.setContentText(String.valueOf(e));
            noPart.showAndWait();
        }
    }

    // Add sort+filter functionality?
    public void updateCustomers() throws Exception {
        customerTable.setItems(CUSTOMER_DAO.getCustomers());
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.HOME, USER);
    }

    public void createButtonPressed(ActionEvent event) throws IOException {

    }

    public void viewButtonPressed(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        SCENE_MANAGER.changeScene(event, SCENE_PATH_CONSTANTS.CUSTOMER, USER, selectedCustomer);
    }

    public void deleteButtonPressed(ActionEvent event) throws IOException {

    }

}
