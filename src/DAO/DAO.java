package DAO;

import util.DatabaseConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO {

    protected DatabaseConnectionUtil dbconn;

    /**
     * User Data Access Object (UserDAO) constructor
     */
    public DAO() {
        DatabaseConnectionUtil dbconn = new DatabaseConnectionUtil();
        try {
            dbconn.connect();
        } catch (Exception e) {
            System.out.println("UserDAO.constructor: No database connected");
            e.printStackTrace();
        }
        this.dbconn = dbconn;
    }

    protected ResultSet executeQuery(PreparedStatement query) {
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

    protected boolean executeUpdate(PreparedStatement updateQuery) {
        try {
            int updatedRows = dbconn.executeUpdate(updateQuery);
            return updatedRows > 0;
        } catch (Exception e) {
            error(e);
            return false;
        }
    }

    protected boolean resultSetIsValid(ResultSet rs) {
        return rs != null;
    }

    protected void error(Exception error) {
        System.out.println(getErrorMessage(error.getMessage()));
        error.printStackTrace();
    }

    protected PreparedStatement getPreparedStatement(String query) {
        try {
            return dbconn.getDBConn().prepareStatement(query);
        } catch (Exception e) {
            return null;
        }
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
