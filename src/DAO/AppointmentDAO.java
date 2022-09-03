package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Appointment;
import scheduler.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentDAO extends DAO {

    public AppointmentDAO() {
        super();
    }

    public Appointment getAppointment(int appointmentId) {
        String query = String.format("SELECT \n" +
                        "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                        "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                        "\tUser_ID, Customer_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d",
                appointmentId);
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new Appointment(
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
                );
            }
        } catch (SQLException sqle) {
            error(sqle);
            return null;
        }
        return null;
    }

    public boolean updateAppointment(Appointment appointment, User user) {
        String updateQuery = "UPDATE appointments\n" +
                "SET\n" +
                "\tTitle = \"%s\",\n" +
                "\tDescription = \"%s\",\n" +
                "\tLocation = \"%s\",\n" +
                "\tType = \"%s\",\n" +
                "\tStart = ?,\n" +
                "\tEnd = ?,\n" +
                "\tCreate_Date = ?,\n" +
                "\tCreated_By = \"%s\"\n,\n" +
                "\tLast_Update = ?,\n" +
                "\tLast_Updated_By = \"%s\",\n" +
                "\tUser_ID = \"%d\",\n" +
                "\tCustomer_ID = \"%d\",\n" +
                "\tContact_ID = \"%d\"\n" +
                "WHERE Appointment_ID = %d;";
        updateQuery = String.format(
                updateQuery,
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.getLocation(),
                appointment.getType(),
                user.getUsername(),
                user.getUsername(),
                user.getUserID(),
                appointment.getCustomerId(),
                appointment.getContactId(),
                appointment.getAppointmentId()
        );
        PreparedStatement ps = getPreparedStatement(updateQuery);
        try {
            ps.setTimestamp(1, appointment.getStart());
            ps.setTimestamp(2, appointment.getEnd());
            ps.setTimestamp(3, appointment.getCreateDate());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        return executeUpdate(ps);
    }

    public boolean insertAppointment(Appointment appointment, User user) {
        String insertQuery = "INSERT INTO customers (\n" +
                "\tTitle, Description, Location, Type, Start, End, Create_Date, Created_By,\n" +
                "\tLast_Update, Last_Updated_By, User_ID, Customer_ID, Contact_ID\n" +
                ")\n" +
                "VALUES (\n" +
                "\t\"%s\",\n" +
                "\t\"%s\",\n" +
                "\t\"%s\",\n" +
                "\t\"%s\",\n" +
                "\t?,\n" +
                "\t?,\n" +
                "\t?,\n" +
                "\t\"%s\",\n" +
                "\t?,\n" +
                "\t\"%s\",\n" +
                "\t%d\n" +
                "\t%d\n" +
                "\t%d\n" +
                ");";
        insertQuery = String.format(
                insertQuery,
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.getLocation(),
                appointment.getType(),
                user.getUsername(),
                user.getUsername(),
                user.getUserID(),
                appointment.getCustomerId(),
                appointment.getContactId()
        );
        PreparedStatement ps = getPreparedStatement(insertQuery);
        try {
            ps.setTimestamp(1, appointment.getStart());
            ps.setTimestamp(2, appointment.getEnd());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        return executeUpdate(ps);
    }

    public ObservableList<Appointment> getUserAppointments(User user) {
        String query = String.format("SELECT \n" +
                        "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                        "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                        "\tUser_ID, Customer_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d",
                user.getUserID());
        ResultSet rs = executeQuery(getPreparedStatement(query));
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

    public ObservableList<Appointment> getUserAppointmentsInRange(User user,
                                                                  Timestamp start,
                                                                  Timestamp end) {
        String query = String.format("SELECT \n" +
                        "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                        "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                        "\tUser_ID, Customer_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d" +
                        "AND (\n" +
                        "\tStart BETWEEN ? AND ?\n" +
                        ");",
                user.getUserID());
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        PreparedStatement ps = getPreparedStatement(query);
        try {
            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
        } catch (SQLException sqle) {
            error(sqle);
            return appointments;
        }
        ResultSet rs = executeQuery(ps);
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

    private boolean customerHasAppointments(int customerId) {
        String query = String.format(
                "SELECT COUNT(Appointment_ID)\n" +
                        "FROM appointments\n" +
                        "WHERE Customer_ID = %d;",
                customerId
        );
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        return false;
    }

    public boolean userHasAppointmentsInRange(int userId, Timestamp start, Timestamp end) {
        String query = String.format(
                "Select COUNT(Appointment_ID)\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d\n" +
                        "AND (\n" +
                        "\tStart BETWEEN ? AND ? OR\n" +
                        "\tEnd BETWEEN ? AND ?\n" +
                        ");",
                userId
        );
        PreparedStatement ps = getPreparedStatement(query);
        try {
            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        ResultSet rs = executeQuery(ps);
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        return false;
    }

    public boolean customerHasAppointmentsInRange(int customerId, Timestamp start, Timestamp end) {
        String query = String.format(
                "Select COUNT(Appointment_ID)\n" +
                        "FROM appointments\n" +
                        "WHERE Customer_ID = %d\n" +
                        "AND (\n" +
                        "\tStart BETWEEN ? AND ? OR\n" +
                        "\tEnd BETWEEN ? AND ?\n" +
                        ");",
                customerId
        );
        PreparedStatement ps = getPreparedStatement(query);
        try {
            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        ResultSet rs = executeQuery(ps);
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException sqle) {
            error(sqle);
            return false;
        }
        return false;
    }

    public boolean deleteCustomerAppointments(int customerId) {
        String deleteQuery = String.format(
                "DELETE FROM appointments\n" +
                        "WHERE Customer_ID = %d;",
                customerId
        );
        if (customerHasAppointments(customerId)) {
            return executeUpdate(getPreparedStatement(deleteQuery));
        } else {
            return true;
        }
    }
}
