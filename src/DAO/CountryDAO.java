package DAO;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO extends DAO {

    public CountryDAO() {
        super();
    }

    /**
     * Retrieves all countries
     *
     * @return ObservableList of String country names
     */
    public ObservableList<String> getAllCountries() {
        String query = "SELECT Country\n" +
                "FROM countries;";
        ResultSet rs = executeQuery(getPreparedStatement(query));
        if (resultSetIsValid(rs)) {
            try {
                ObservableList<String> countries = FXCollections.observableArrayList();
                while (rs.next()) {
                    countries.add(rs.getString(1));
                }
                return countries;
            } catch (SQLException e) {
                error(e);
                return null;
            }
        }
        return null;
    }

    /**
     * Retrieves all the first level divisions for a provided country string
     *
     * @param country - String value representing country name
     * @return - ObservableList of string first level divisions
     */
    public ObservableList<String> getCountryDivisions(String country) {
        String query = "SELECT Division\n" +
                "FROM countries a\n" +
                "LEFT JOIN first_level_divisions b\n" +
                "ON a.Country_ID = b.Country_ID\n" +
                String.format("WHERE a.Country = \"%s\";", country);
        ResultSet rs = executeQuery(getPreparedStatement(query));
        if (resultSetIsValid(rs)) {
            try {
                ObservableList<String> divisions = FXCollections.observableArrayList();
                while (rs.next()) {
                    divisions.add(rs.getString(1));
                }
                return divisions;
            } catch (SQLException e) {
                error(e);
                return null;
            }
        }
        return null;
    }
}
