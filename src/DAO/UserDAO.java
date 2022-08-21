package DAO;

import util.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO {

    /*
     * Constructor calls abstract class constructor
     */
    public UserDAO() {
        super();
    }

    /**
     * Method to verify that a user is in the database, and if so, that the password matches
     * @param username the username of the user to verify
     * @param password the password of the user to verify
     * @return true if the user exists with an equivalent password
     * @throws Exception if a ResultSet is not returned indicating a database connection error
     */
    public boolean verifyUser(String username, String password) {

        // Add logging for successful and unsuccessful login attempts
        String query = String.format("SELECT Password FROM USERS WHERE User_Name = \"%s\";", username);
        ResultSet rs = executeQuery(query);
        if (resultSetIsValid(rs)) {
            try {
                String storedPassword = rs.next() ? rs.getString(1) : null;
                if (storedPassword == null || !storedPassword.equals(password)) {
                    return false;
                }
                return true;
            } catch (SQLException e) {
                error(e);
            }
        }
        return false;
    }
}
