package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO extends DAO {

    /**
     * Retrieves the appointments by month and type report
     *
     * @return - ObservableList containing string arrays representing each row
     */
    public ObservableList<String[]> getAppointmentCountByTypeAndMonth() {
        String query = "Select Type, CONCAT(MONTH(Start), \"-\", YEAR(Start)), COUNT(*)\n" +
                "FROM appointments\n" +
                "GROUP BY Type, MONTH(Start), YEAR(Start)\n" +
                "ORDER BY YEAR(Start) DESC, MONTH(Start) DESC;";
        ObservableList<String[]> report = FXCollections.observableArrayList();
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs)) {
                while (rs.next()) {
                    String[] row = {
                            rs.getString(1),
                            rs.getString(2),
                            String.valueOf(rs.getInt(3))
                    };
                    report.add(row);
                }
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return report;
    }

    /**
     * Get a specified contact's appointment schedule
     *
     * @param contactId - Integer contact ID
     * @return - Contact appointment schedule
     */
    public ObservableList<Appointment> getContactScheduleReport(int contactId) {
        String query = "SELECT \n" +
                "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                "\tUser_ID, Customer_ID, Contact_ID\n" +
                "FROM appointments\n" +
                "WHERE Contact_ID = %d\n" +
                "ORDER BY Start DESC;";
        ResultSet rs = executeQuery(getPreparedStatement(String.format(query, contactId)));
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            if (resultSetIsValid(rs)) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getTimestamp(6),
                            rs.getTimestamp(7),
                            rs.getTimestamp(8),
                            rs.getString(9),
                            rs.getTimestamp(10),
                            rs.getString(11),
                            rs.getInt(12),
                            rs.getInt(13),
                            rs.getInt(14)
                    ));
                }
                return appointments;
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return appointments;
    }

    /**
     * Retrieves the third report - the customer appointment count report
     *
     * @return - ObservableList containing string arrays representing each object
     */
    public ObservableList<String[]> getCustomerQuantityReport() {
        String query = "SELECT Customer_ID, COUNT(*)\n" +
                "FROM appointments\n" +
                "GROUP BY Customer_ID\n" +
                "ORDER BY 1 ASC;";
        ObservableList<String[]> report = FXCollections.observableArrayList();
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs)) {
                while (rs.next()) {
                    String[] row = {
                            String.valueOf(rs.getInt(1)),
                            String.valueOf(rs.getInt(2))
                    };
                    report.add(row);
                }
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return report;
    }
}
