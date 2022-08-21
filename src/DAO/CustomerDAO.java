package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Customer;
import util.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
        ResultSet rs = executeQuery(query);
        if (resultSetIsValid(rs)) {
            try {
                while (rs.next()) {
                    customers.add(new Customer(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getTimestamp(6).toLocalDateTime(),
                            rs.getString(7),
                            rs.getTimestamp(8).toLocalDateTime(),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11)
                    ));
                }
                return customers;
            } catch (SQLException e) {
                error(e);
                return null;
            }
        }
        return null;
    }

    public boolean updateCustomer(Customer customer, String user) {
        // Add Division ID to update query
        String updateQuery = "UPDATE customers\n" +
            "SET\n" +
            "\tCustomer_Name = \"%s\",\n" +
            "\tAddress = \"%s\",\n" +
            "\tPostal_Code = \"%s\",\n" +
            "\tPhone = \"%s\",\n" +
            "\tLast_Updated_By = \"%s\",\n" +
            "\tLast_Update = \"%s\"\n" +
            "WHERE Customer_ID = %d;\n";
        updateQuery= String.format(
            updateQuery,
            customer.getName(),
            customer.getAddress(),
            customer.getPostalCode(),
            customer.getPhone(),
            user,
            Timestamp.valueOf(LocalDateTime.now()),
            customer.getCustomerId()
        );
        System.out.println(updateQuery);
        return executeUpdate(updateQuery);
    }
}