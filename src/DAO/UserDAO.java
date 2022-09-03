package DAO;

import scheduler.User;

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
     *
     * @param username the username of the user to verify
     * @param password the password of the user to verify
     * @return true if the user exists with an equivalent password
     */
    public boolean verifyUser(String username, String password) {
        // Add logging for successful and unsuccessful login attempts
        String query = String.format("SELECT Password FROM USERS WHERE User_Name = \"%s\";", username);
        ResultSet rs = executeQuery(getPreparedStatement(query));
        if (resultSetIsValid(rs)) {
            try {
                String storedPassword = rs.next() ? rs.getString(1) : null;
                return storedPassword != null && storedPassword.equals(password);
            } catch (SQLException e) {
                error(e);
            }
        }
        return false;
    }

    /**
     * Retrieves a specific user by ID
     *
     * @param id - Integer ID of the user to retrieve
     * @return - {@link User} retrieved
     */
    public User getUser(int id) {
        String query = String.format(
                "SELECT User_Name\n" +
                        "FROM users\n" +
                        "WHERE User_ID = %d;",
                id
        );
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new User(rs.getString(1), id);
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return null;
    }

    /**
     * Retrieves a specific user by ID
     *
     * @param username - String representing username of the user to retrieve
     * @return - {@link User} retrieved
     */
    public User getUser(String username) {
        String query = String.format(
                "SELECT User_ID\n" +
                        "FROM users\n" +
                        "WHERE User_Name = \"%s\";",
                username
        );
        ResultSet rs = executeQuery(getPreparedStatement(query));
        try {
            if (resultSetIsValid(rs) && rs.next()) {
                return new User(username, rs.getInt(1));
            }
        } catch (SQLException sqle) {
            error(sqle);
        }
        return null;
    }
}
