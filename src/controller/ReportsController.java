package controller;

import DAO.ReportDAO;
import constants.ScenePathConstants;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.Appointment;
import scheduler.Contact;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController extends Controller{

    @FXML
    private TableView<String[]> quantityReportTV;

    @FXML
    private TableColumn<String[], String> quantityReportTypeCol;

    @FXML
    private TableColumn<String[], String> quantityReportMonthCol;

    @FXML
    private TableColumn<String[], Integer> quantityReportQuantityCol;

    @FXML
    private TableView<Appointment> contactScheduleTV;

    @FXML
    private TableColumn<Appointment, Integer> contactScheduleIdCol;

    @FXML
    private TableColumn<Appointment, String> contactScheduleTitleCol;

    @FXML
    private TableColumn<Appointment, String> contactScheduleTypeCol;

    @FXML
    private TableColumn<Appointment, String> contactScheduleDescriptionCol;

    @FXML
    private TableColumn<Appointment, String> contactScheduleStartCol;

    @FXML
    private TableColumn<Appointment, String> contactScheduleEndCol;

    @FXML
    private TableColumn<Appointment, Integer> contactScheduleCustomerCol;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private TableView<String[]> customerQuantityReportTV;

    @FXML
    private TableColumn<String[], Integer> customerQuantityReportIdCol;

    @FXML
    private TableColumn<String[], String> customerQuantityReportNameCol;

    @FXML
    private TableColumn<String[], Integer> customerQuantityReportQuantityCol;

    @FXML
    private Button backButton;

    @FXML
    private Button refreshButton;

    public void initialize(URL url, ResourceBundle rb) {
        contactComboBox.setItems(CONTACT_DAO.getContacts());
        contactComboBox.getSelectionModel().select(0);
        setQuantityReportTV();
        setContactScheduleTV();
        setCustomerQuantityReport();
    }

    public void backButtonPressed(ActionEvent event) throws IOException {
        SCENE_MANAGER.changeScene(event, ScenePathConstants.HOME, USER);
    }

    /**
     * Sets the quantity report by type and month table view
     */
    public void setQuantityReportTV() {
        quantityReportTypeCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue()[0])
        );
        quantityReportMonthCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue()[1])
        );
        quantityReportQuantityCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(Integer.parseInt(cellData.getValue()[2])).asObject()
        );
        quantityReportTV.setItems(REPORT_DAO.getAppointmentCountByTypeAndMonth());
    }

    /**
     * Sets the contact schedule tableview based on the selected customer
     */
    public void setContactScheduleTV() {
        contactScheduleIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                cellData.getValue().getContactId()).asObject()
        );
        contactScheduleTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getTitle())
        );
        contactScheduleTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getType())
        );
        contactScheduleCustomerCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(
                        CUSTOMER_DAO.getCustomer(cellData.getValue().getCustomerId()).getCustomerId()
                ).asObject()
        );
        contactScheduleDescriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDescription())
        );
        contactScheduleStartCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        formatDatetime(cellData.getValue().getStart())
                )
        );
        contactScheduleEndCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        formatDatetime(cellData.getValue().getEnd())
                )
        );
        contactScheduleTV.setItems(REPORT_DAO.getContactScheduleReport(
                contactComboBox.getSelectionModel().getSelectedItem().getContactId()
        ));
    }

    /**
     * Sets the customer appointment quantity table view
     */
    public void setCustomerQuantityReport() {
        customerQuantityReportIdCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(Integer.parseInt(cellData.getValue()[0])).asObject()
        );
        customerQuantityReportNameCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        CUSTOMER_DAO.getCustomer(Integer.parseInt(cellData.getValue()[0])).getName()
                )
        );
        customerQuantityReportQuantityCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(Integer.parseInt(cellData.getValue()[1])).asObject()
        );
        customerQuantityReportTV.setItems(REPORT_DAO.getCustomerQuantityReport());
    }

    /**
     * Updates the contact schedule table view when the contact combo box selection is changed
     */
    public void contactChange() {
        setContactScheduleTV();
    }

    /**
     * Refreshes all reports
     */
    public void refreshButtonPressed() {
        setQuantityReportTV();
        setContactScheduleTV();
        setCustomerQuantityReport();
    }
}
