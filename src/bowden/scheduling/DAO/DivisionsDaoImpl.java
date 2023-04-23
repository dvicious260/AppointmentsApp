package bowden.scheduling.DAO;

import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Model.Countries;
import bowden.scheduling.Model.FirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 This class provides implementation for performing database operations related to first level divisions.
 */
public class DivisionsDaoImpl {

    /**
     Retrieves a list of first level divisions for a given country ID.
     @param countryId The ID of the country for which to retrieve divisions.
     @return An ObservableList of FirstLevelDivisions objects representing the divisions for the given country ID.
     */
    public static ObservableList<FirstLevelDivisions> getDivisionsByCountryId(int countryId) {
        ObservableList<FirstLevelDivisions> divisions = FXCollections.observableArrayList();

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Division_ID, Division, Country_ID FROM first_level_divisions WHERE Country_ID = ?")) {
            statement.setInt(1, countryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("Division_ID");
                    String name = resultSet.getString("Division");
                    int countryId1 = resultSet.getInt("Country_ID");

                    divisions.add(new FirstLevelDivisions(id, name, countryId1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return divisions;
    }

    /**
     Retrieves a first level division object by ID.
     @param id The ID of the division to retrieve.
     @return A FirstLevelDivisions object representing the division with the given ID, or null if not found.
     @throws SQLException if an error occurs while performing the database query.
     */
    public static FirstLevelDivisions getDivisionById(int id) throws SQLException {
        FirstLevelDivisions division = null;

        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.openConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("COUNTRY_ID");

            division = new FirstLevelDivisions(divisionId, divisionName, countryId);
        }

        return division;
    }
}
