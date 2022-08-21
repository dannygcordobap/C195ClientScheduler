package DAO;

import util.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO {
    protected DatabaseConnection dbconn;
    /**
     * User Data Access Object (UserDAO) constructor
     */
    public DAO() {
        DatabaseConnection dbconn = new DatabaseConnection();
        try {
            dbconn.connect();
        } catch (Exception e) {
            System.out.println("UserDAO.constructor: No database connected");
            e.printStackTrace();
        }
        this.dbconn = dbconn;
    }

    protected ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            rs = dbconn.executeQuery(query);
        } catch (SQLException sqle) {
            System.out.println();
            sqle.printStackTrace();
        } catch (Exception e) {
            System.out.println("CustomerDAO.getCustomers: No database connected");
            e.printStackTrace();
        }
        return rs;
    }

    protected boolean executeUpdate(String updateQuery) {
        try {
            int updatedRows = dbconn.executeUpdate(updateQuery);
            if (updatedRows > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            error(e);
            return false;
        }
    }

    protected boolean resultSetIsValid(ResultSet rs) {
        if (rs == null) {
            return false;
        }
        return true;
    }

    protected void error(Exception error) {
        System.out.println(getErrorMessage(error.getMessage()));
        error.printStackTrace();
    }

    private String getErrorMessage(String errorMessage) {
        return String.format(
                "%s.%s: %s",
                getClass(),
                new Exception().getStackTrace()[2].getMethodName(),
                errorMessage
        );
    }
}
