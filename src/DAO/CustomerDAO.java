package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Customer;
import scheduler.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CustomerDAO extends DAO {

    public CustomerDAO() {
        super();
    }

    public ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT  \n" +
                "\ta.Customer_ID, a.Customer_Name, a.Address, a.Postal_Code, \n" +
                "\ta.Phone, a.Create_Date, a.Created_By, a.Last_Update, \n" +
                "\ta.Last_Updated_By, b.Division, c.Country\n" +
                "FROM customers a\n" +
                "LEFT JOIN first_level_divisions b\n" +
                "ON a.Division_ID = b.Division_ID\n" +
                "LEFT JOIN countries c\n" +
                "ON b.Country_ID = c.Country_ID\n" +
                "ORDER BY 2 ASC;";
        ResultSet rs = executeQuery(getPreparedStatement(query));
        if (resultSetIsValid(rs)) {
            try {
                while (rs.next()) {
                    customers.add(new Customer(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getTimestamp(6),
                            rs.getString(7),
                            rs.getTimestamp(8),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11)
                    ));
                }
                return customers;
            } catch (SQLException e) {
                error(e);
                return customers;
            }
        }
        return customers;
    }

    public Customer getCustomer(int customerId) {
        String query = "SELECT  \n" +
                "\ta.Customer_ID, a.Customer_Name, a.Address, a.Postal_Code, \n" +
                "\ta.Phone, a.Create_Date, a.Created_By, a.Last_Update, \n" +
                "\ta.Last_Updated_By, b.Division, c.Country\n" +
                "FROM customers a\n" +
                "LEFT JOIN first_level_divisions b\n" +
                "ON a.Division_ID = b.Division_ID\n" +
                "LEFT JOIN countries c\n" +
                "ON b.Country_ID = c.Country_ID\n" +
                "WHERE a.Customer_ID = %d;";
        ResultSet rs = executeQuery(getPreparedStatement(String.format(query, customerId)));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)
                );
            }
        } catch (SQLException sqle) {
            error(sqle);
            return null;
        }
        return null;
    }

    /**
     * Retrieves a customer using a customer object
     * @param customer - Customer object to retrieve from database
     * @return - customer
     */
    public Customer getCustomer(Customer customer) {
        String query = "SELECT\n" +
                "\ta.Customer_ID, a.Customer_Name, a.Address, a.Postal_Code,\n" +
                "\ta.Phone, a.Create_Date, a.Created_By, a.Last_Update,\n" +
                "\ta.Last_Updated_By, b.Division, c.Country\n" +
                "FROM customers a\n" +
                "LEFT JOIN first_level_divisions b\n" +
                "ON a.Division_ID = b.Division_ID\n" +
                "LEFT JOIN countries c\n" +
                "ON b.Country_ID = c.Country_ID\n" +
                "WHERE\n" +
                "\ta.Customer_Name = \"%s\" AND a.Address = \"%s\" AND\n" +
                "\ta.Postal_Code = \"%s\" AND a.Phone = \"%s\" AND\n" +
                "\ta.Division_ID = %d;";
        query = String.format(
                query,
                customer.getName(),
                customer.getAddress(),
                customer.getPostalCode(),
                customer.getPhone(),
                getDivisionID(customer.getDivision())
        );
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)
                );
            }
        } catch (SQLException sqle) {
            error(sqle);
            return null;
        }
        return null;
    }

    public int getDivisionID(String division) {
        int id = -1;
        String query = "SELECT Division_ID \n" +
                "FROM first_level_divisions\n" +
                "WHERE Division = \"%s\";";
        ResultSet rs = executeQuery(getPreparedStatement(String.format(query, division)));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            error(e);
            return id;
        }
        return id;
    }

    public boolean updateCustomer(Customer customer, User user) {
        int divisionId = getDivisionID(customer.getDivision());
        if (divisionId > 0) {
            String updateQuery = "UPDATE customers\n" +
                    "SET\n" +
                    "\tCustomer_Name = \"%s\",\n" +
                    "\tAddress = \"%s\",\n" +
                    "\tPostal_Code = \"%s\",\n" +
                    "\tPhone = \"%s\",\n" +
                    "\tLast_Updated_By = \"%s\",\n" +
                    "\tLast_Update = ?,\n" +
                    "\tDivision_ID = %d\n" +
                    "WHERE Customer_ID = %d;\n";
            updateQuery = String.format(
                    updateQuery,
                    customer.getName(),
                    customer.getAddress(),
                    customer.getPostalCode(),
                    customer.getPhone(),
                    user.getUsername(),
                    divisionId,
                    customer.getCustomerId()
            );
            PreparedStatement ps = getPreparedStatement(updateQuery);
            try {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            } catch (SQLException sqle) {
                error(sqle);
                return false;
            }
            return executeUpdate(ps);
        }
        return false;
    }

    public boolean insertCustomer(Customer customer, User user) {
        int divisionId = getDivisionID(customer.getDivision());
        if (divisionId > 0) {
            String insertQuery = "INSERT INTO customers (\n" +
                    "\tCustomer_Name, Address, Postal_Code, Phone,\n" +
                    "\tCreate_Date, Created_By, Last_Update,\n" +
                    "\tLast_Updated_By, Division_ID\n" +
                    ")\n" +
                    "VALUES (\n" +
                    "\t\"%s\",\n" +
                    "\t\"%s\",\n" +
                    "\t\"%s\",\n" +
                    "\t\"%s\",\n" +
                    "\t?,\n" +
                    "\t\"%s\",\n" +
                    "\t?,\n" +
                    "\t\"%s\",\n" +
                    "\t%d\n" +
                    ");";
            insertQuery = String.format(
                    insertQuery,
                    customer.getName(),
                    customer.getAddress(),
                    customer.getPostalCode(),
                    customer.getPhone(),
                    user.getUsername(),
                    user.getUsername(),
                    divisionId
            );
            PreparedStatement ps = getPreparedStatement(insertQuery);
            try {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } catch (SQLException sqle) {
                error(sqle);
                return false;
            }
            return executeUpdate(ps);
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) {
        String deleteQuery = String.format(
                "DELETE FROM customers\n" +
                        "WHERE Customer_ID = %d;\n",
                customerId
        );
        return executeUpdate(getPreparedStatement(deleteQuery));
    }
}