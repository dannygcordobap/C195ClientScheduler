package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Appointment;
import scheduler.Customer;
import scheduler.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentDAO extends DAO {

    /**
     * Appointment DAO constructor
     */
    public AppointmentDAO() {
        super();
    }

    /**
     * Retrieves an appointment by ID
     *
     * @param appointmentId - Appointment ID to retrieve
     * @return {@link Appointment}
     */
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

    /**
     *  Gets an appointment with an appointments objects attributes
     *
     * @param appointment - Appointment object to get from database
     * @return - Appointment
     */
    public Appointment getAppointment(Appointment appointment) {
        String query = "SELECT\n" +
                "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                "\tUser_ID, Customer_ID, Contact_ID\n" +
                "FROM appointments\n" +
                "WHERE\n" +
                "\tTitle = \"%s\" AND Description = \"%s\" AND Location = \"%s\" AND Type = \"%s\" AND\n" +
                "\tStart = ? AND End = ? AND Customer_ID = %d AND Contact_ID = %d;";
        query = String.format(
                query,
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.getLocation(),
                appointment.getType(),
                appointment.getCustomerId(),
                appointment.getContactId()
        );
        PreparedStatement ps = getPreparedStatement(query);
        try {
            ps.setTimestamp(1, appointment.getStart());
            ps.setTimestamp(2, appointment.getEnd());
        } catch (SQLException sqle) {
            error(sqle);
            return null;
        }
        ResultSet rs = executeQuery(ps);
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

    /**
     * Updates an appointment object in the database
     *
     * @param appointment - Appointment object to update
     * @param user - The acting user
     * @return - boolean success indicator
     */
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

    /**
     * Deletes an appointment by ID
     *
     * @param appointmentId - Appointment ID to delete
     * @return - boolean success indicator
     */
    public boolean deleteAppointment(int appointmentId) {
        String query = String.format("DELETE \n" +
                        "FROM appointments\n" +
                        "WHERE Appointment_ID = %d",
                appointmentId);
        return executeUpdate(getPreparedStatement(query));
    }

    /**
     * Inserts an appointment to the database
     *
     * @param appointment - Appointment object to add to the database
     * @param user - Logged in user
     * @return - boolean success indicator
     */
    public boolean insertAppointment(Appointment appointment, User user) {
        String insertQuery = "INSERT INTO appointments (\n" +
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
                "\t%d,\n" +
                "\t%d,\n" +
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
        System.out.println(insertQuery);
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

    /**
     * Gets all of a users appointments
     *
     * @param user - Logged in user
     * @return - ObservableList of Appointments
     */
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

    /**
     * Gets all of a users appointments with in a specified date range.
     * Inclusive of start and end
     *
     * @param user - Logged in user
     * @param start - Start timestamp (inclusive)
     * @param end - End timestamp (inclusive)
     * @return - ObservableList of appointments
     */
    public ObservableList<Appointment> getUserAppointmentsInRange(User user,
                                                                  Timestamp start,
                                                                  Timestamp end) {
        String query = String.format("SELECT \n" +
                        "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                        "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                        "\tUser_ID, Customer_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d\n" +
                        "AND (\n" +
                        "\tStart BETWEEN ? AND ? OR\n" +
                        "\tEnd BETWEEN ? AND ?\n" +
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

    /**
     * Gets a customer's appointments within a given range.
     * Inclusive of the start and end
     *
     * @param customerId - Customer ID of the customer in question
     * @param start - Start timestamp
     * @param end - End timestamp
     * @return - ObservableList of Appointments
     */
    public ObservableList<Appointment> getCustomerAppointmentsInRange(int customerId,
                                                                      Timestamp start,
                                                                      Timestamp end) {
        String query = String.format("SELECT \n" +
                        "\tAppointment_ID, Title, Description, Location, Type, Start,\n" +
                        "\tEnd, Create_Date, Created_By, Last_Update, Last_Updated_By,\n" +
                        "\tUser_ID, Customer_ID, Contact_ID\n" +
                        "FROM appointments\n" +
                        "WHERE User_ID = %d\n" +
                        "AND (\n" +
                        "\tStart BETWEEN ? AND ? OR\n" +
                        "\tEnd BETWEEN ? AND ?\n" +
                        ");",
                customerId);
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

    /**
     * Function to check if a customer has appointments
     *
     * @param customerId - Customer ID for customer in question
     * @return - boolean indicator
     */
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

    /**
     * Helper function to determine if a user has appointments within a range
     *
     * @param user - Logged in user
     * @param start - Start timestamp
     * @param end - End timestamp
     * @return boolean indicator
     */
    public boolean userHasAppointmentsInRange(User user, Timestamp start, Timestamp end) {
        return !getUserAppointmentsInRange(user, start, end).isEmpty();
    }

    /**
     * Helper function to determine if a customer has appointments within a range
     *
     * @param customerId - Customer ID of customer in question
     * @param start - Start timestamp
     * @param end - End timestamp
     * @return - boolean indicator
     */
    public boolean customerHasAppointmentsInRange(int customerId, Timestamp start, Timestamp end) {
        return !getCustomerAppointmentsInRange(customerId, start, end).isEmpty();
    }

    /**
     * Deletes all appointments including a customer
     * @param customerId - Customer ID of the customer who's appointments should be deleted
     * @return - boolean indicator
     */
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

    // TODO: func to get "the total number of customer appointments by type and month" for report
}
