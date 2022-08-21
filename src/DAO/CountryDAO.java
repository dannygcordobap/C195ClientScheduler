package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO extends DAO {

    public CountryDAO() {
        super();
    }

    public ObservableList<String> getAllCountries() throws Exception {
        String query = "SELECT Country\n" +
                "FROM countries;";
        ResultSet rs = super.executeQuery(query);
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

    public ObservableList<String> getCountryDivisions(String country) {
        String query = "SELECT Division\n" +
                "FROM countries a\n" +
                "LEFT JOIN first_level_divisions b\n" +
                "ON a.Country_ID = b.Country_ID\n" +
                String.format("WHERE a.Country = \"%s\";", country);
        ResultSet rs = executeQuery(query);
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
