package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO extends DAO {

    public ContactDAO() {
        super();
    }

    /**
     * Gets all contacts in the database and returns an ObservableList of {@link Contact} objects
     *
     * @return ObservableList of contacts
     */
    public ObservableList<Contact> getContacts() {
        String query = "SELECT Contact_ID, Contact_Name, Email\n" +
                "FROM contacts";
        ResultSet rs = executeQuery(getPreparedStatement(query));
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            if (resultSetIsValid(rs)) {
                while (rs.next()) {
                    contacts.add(new Contact(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3)
                    ));
                }
            }
        } catch (SQLException sqle) {
            error(sqle);

        }
        return contacts;
    }

    /**
     * Retrieves a specific contact from the database by contact ID
     *
     * @param contactId - Contact ID of the contact to retrieve
     * @return - {@link Contact} object
     */
    public Contact getContact(int contactId) {
        String query = String.format(
                "SELECT Contact_Name, Email\n" +
                        "FROM contacts\n" +
                        "WHERE Contact_ID = %d;",
                contactId
        );
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new Contact(contactId, rs.getString(1), rs.getString(2));
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return null;
    }
}
