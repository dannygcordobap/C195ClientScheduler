package util;

import java.sql.*;

public class DatabaseConnectionUtil {

    public final String dbName = "client_schedule";
    public final String dbUsername = "sqlUser";
    public final String dbPassword = "Passw0rd!";
    private final String databaseURL = "jdbc:mysql://localhost:3306/" + this.dbName + "?connectionTimeZone=SERVER";

    private Connection conn;

    // Database connection getter
    public Connection getDBConn() throws Exception {
        Connection conn = this.conn;
        if (conn == null) {
            throw new Exception("No database connected");
        }
        return conn;
    }

    /**
     * Database connection class constructor
     */
    public DatabaseConnectionUtil() {
        try {
            connect();
        } catch (Exception e) {
            System.out.println("DB Connection Error");
        }
    }

    /**
     * Method to connect to database with the credentials provided during initialization
     *
     * @return DatabaseConnection object
     * @throws Exception upon invalid connection
     */
    public void connect() throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Sets and loads the appropriate JDBC driver
            this.conn = DriverManager.getConnection(
                    this.databaseURL, this.dbUsername, this.dbPassword
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to execute select-from-where SQL queries
     *
     * @param query the SQL query to execute as a string
     * @return ResultSet containing the results of the query
     * @throws SQLException if the sql statement passed in is invalid
     * @throws Exception    if the database connection variable is null
     */
    public ResultSet executeQuery(PreparedStatement ps) throws SQLException, Exception {
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**
     * Method to excute queries other than select-from-where queries
     *
     * @param query the SQL query to execute
     * @return int representing the number of affected rows
     * @throws SQLException if the SQL statement passed in is invalid
     * @throws Exception    if the database connection variable is null
     */
    public int executeUpdate(PreparedStatement ps) throws SQLException, Exception {
        int updatedRows = ps.executeUpdate();
        return updatedRows;
    }
}
