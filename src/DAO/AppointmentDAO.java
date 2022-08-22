package DAO;

public class AppointmentDAO extends DAO {

    public AppointmentDAO() { super(); }

    public boolean deleteCustomerAppointments(int customerId) {
        String deleteQuery = String.format(
                "DELETE FROM appointments\n" +
                        "WHERE Customer_ID = %d;",
                customerId
        );
        return executeUpdate(deleteQuery);
    }
}
