package bowden.scheduling.DAO;

import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 The CountriesDaoImpl class provides methods to retrieve all countries and get a country by ID from the database.
 */
public class CountriesDaoImpl {

    /**
     * Retrieves all countries from the database.
     *
     * @return an ObservableList of all countries.
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countries = FXCollections.observableArrayList();

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM countries");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Country_ID");
                String name = resultSet.getString("Country");

                countries.add(new Countries(id, name));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return countries;
    }

    /**
     * Retrieves a country by its ID from the database.
     *
     * @param countryID the ID of the country to retrieve.
     * @return the country with the specified ID, or null if no such country exists.
     * @throws SQLException if there is an error accessing the database.
     */
    public static Countries getCountryById(int countryID) throws SQLException {
        Connection conn = JDBC.openConnection();
        String query = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String countryName = rs.getString("Country");
            return new Countries(countryID, countryName);
        }
        return null;
    }


}