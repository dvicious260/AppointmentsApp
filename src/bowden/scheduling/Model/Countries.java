package bowden.scheduling.Model;

import bowden.scheduling.Helper.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Countries class represents a country and provides methods to get and set
 * the country ID and name. It also provides a method to search for a matching country in the database.
 */
public class Countries {
    int countryID;
    String countryName;

    /**
     * Creates a new Countries instance with the specified ID and name.
     *
     * @param countryID   the ID of the country
     * @param countryName the name of the country
     */
    public Countries(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * Returns the ID of the country.
     *
     * @return the ID of the country
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Sets the ID of the country.
     *
     * @param countryID the ID of the country
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Returns the name of the country.
     *
     * @return the name of the country
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the name of the country.
     *
     * @param countryName the name of the country
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Returns the name of the country.
     *
     * @return the name of the country
     */
    @Override
    public String toString() {
        return countryName;
    }

    /**
     * Searches the database for a country that matches the specified country name.
     *
     * @param country the country to search for
     * @return a Countries instance representing the matching country, or null if no matching country is found
     */
    public static Countries getCountry(Countries country) {
        Countries matchingCountry = null;

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM countries WHERE Country = ?")) {
            statement.setString(1, country.getCountryName());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("Country_ID");
                    String name = resultSet.getString("Country");

                    matchingCountry = new Countries(id, name);
                    break; // Stop searching after finding the first match
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return matchingCountry;
    }
}
